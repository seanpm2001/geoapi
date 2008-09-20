/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2004-2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.referencing;

import java.util.Set;
import java.util.List;
import org.opengis.Validator;
import org.opengis.parameter.*;


/**
 * Validates {@linkplain ParameterValue}s. This class should not be used directly;
 * use the {@link org.opengis.Validators} convenience static methods instead.
 *
 * @author Martin Desruisseaux (Geomatys)
 * @since GeoAPI 2.2
 */
public class ParameterValidator extends Validator {
    /**
     * The system wide instance used by {@link org.opengis.Validators}. Vendor can replace
     * this instance by some {@code Validator} subclass if some tests need to be overrided.
     */
    public static ParameterValidator instance = new ParameterValidator();

    /**
     * Creates a new validator.
     */
    protected ParameterValidator() {
        super("org.opengis.parameter");
    }

    /**
     * Dispatchs the given object to one of {@code validate} methods.
     *
     * @param object The object to dispatch.
     */
    public void dispatch(final GeneralParameterDescriptor object) {
        if (object instanceof ParameterDescriptor) {
            validate((ParameterDescriptor<?>) object);
        } else if (object instanceof ParameterDescriptorGroup) {
            validate((ParameterDescriptorGroup) object);
        } else {
            ReferencingValidator.instance.validate(object);
        }
    }

    /**
     * Dispatchs the given object to one of {@code validate} methods.
     *
     * @param object The object to dispatch.
     */
    public void dispatch(final GeneralParameterValue object) {
        if (object instanceof ParameterValue) {
            validate((ParameterValue<?>) object);
        } else if (object instanceof ParameterValueGroup) {
            validate((ParameterValueGroup) object);
        }
    }

    /**
     * Validates the given descriptor.
     *
     * @param <T> The class of parameter values.
     * @param object The object to validate, or {@code null}.
     */
    public <T> void validate(final ParameterDescriptor<T> object) {
        if (object == null) {
            return;
        }
        ReferencingValidator.instance.validate(object);
        Class<T> valueClass = object.getValueClass();
        mandatory("ParameterDescriptor: getValueClass() can not return null.", valueClass);
        Set<T> validValues = object.getValidValues();
        if (validValues != null) {
            for (T value : validValues) {
                if (value != null) {
                    assertInstanceOf("ParameterDescriptor: getValidValues() has unexpected element.", valueClass, value);
                }
            }
        }
        Comparable<T> min = object.getMinimumValue();
        if (min != null) {
            assertInstanceOf("ParameterDescriptor: getMinimumValue() returns unexpected value.", valueClass, min);
        }
        Comparable<T> max = object.getMaximumValue();
        if (max != null) {
            assertInstanceOf("ParameterDescriptor: getMaximumValue() returns unexpected value.", valueClass, max);
        }
        assertValidRange("ParameterDescriptor: inconsistent minimum and maximum values.", min, max);
        T def = object.getDefaultValue();
        if (def != null) {
            assertInstanceOf("ParameterDescriptor: getDefaultValue() returns unexpected value.", valueClass, def);
            assertBetween("ParameterDescriptor: getDefaultValue() out of range.", min, max, def);
        }
        assertBetween("ParameterDescriptor: getMinimumOccurs() shall returns 0 or 1.", 0, 1, object.getMinimumOccurs());
        assertEquals("ParameterDescriptor: getMaximumOccurs() shall returns exactly 1.", 1, object.getMaximumOccurs());
    }

    /**
     * Validates the given descriptor.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final ParameterDescriptorGroup object) {
        if (object == null) {
            return;
        }
        ReferencingValidator.instance.validate(object);
        List<GeneralParameterDescriptor> descriptors = object.descriptors();
        mandatory("ParameterDescriptorGroup: descriptors() should not return null.", descriptors);
        if (descriptors != null) {
            for (GeneralParameterDescriptor descriptor : descriptors) {
                assertNotNull("ParameterDescriptorGroup: descriptors() can not contain null element.", descriptor);
                dispatch(descriptor);
                GeneralParameterDescriptor byName = object.descriptor(descriptor.getName().getCode());
                mandatory("ParameterDescriptorGroup: descriptor(String) should returns a value.", byName);
                if (byName != null) {
                    assertSame("ParameterDescriptorGroup: descriptor(String) inconsistent with descriptors().",
                            descriptor, byName);
                }
            }
        }
        int minOccurs = object.getMinimumOccurs();
        assertPositive("ParameterDescriptor: getMinimumOccurs() can not be negative.", minOccurs);
        assertValidRange("ParameterDescriptor: getMaximumOccurs() gives inconsistent range.",
                minOccurs, object.getMaximumOccurs());
    }

    /**
     * Validates the given parameter value.
     *
     * @param <T> The class of parameter values.
     * @param object The object to validate, or {@code null}.
     */
    public <T> void validate(final ParameterValue<T> object) {
        if (object == null) {
            return;
        }
        ParameterDescriptor<T> descriptor = object.getDescriptor();
        mandatory("ParameterValue: must have a descriptor.", descriptor);
        validate(descriptor);
        final T value = object.getValue();
        if (value != null) {
            if (descriptor != null) {
                final Class<T> valueClass = descriptor.getValueClass();
                assertInstanceOf("ParameterValue: getValue() returns unexpected value.", valueClass, value);
                final Set<T> validValues = descriptor.getValidValues();
                if (validValues != null) {
                    assertContains("ParameterValue: getValue() not a member of getValidValues() set.",
                            validValues, value);
                }
                assertBetween("ParameterValue: getValue() is out of bounds.",
                        descriptor.getMinimumValue(), descriptor.getMaximumValue(), value);
            }
        }
    }

    /**
     * Validates the given coordinate system.
     *
     * @param object The object to validate, or {@code null}.
     */
    public void validate(final ParameterValueGroup object) {
        if (object == null) {
            return;
        }
        final ParameterDescriptorGroup descriptor = object.getDescriptor();
        mandatory("ParameterValueGroup: must have a descriptor.", descriptor);
        validate(descriptor);
        List<GeneralParameterValue> values = object.values();
        mandatory("ParameterValueGroup: values() should not return null.", values);
        if (values != null) {
            for (GeneralParameterValue value : values) {
                assertNotNull("ParameterValueGroup: values() can not contain null element.", value);
                dispatch(value);
                if (value instanceof ParameterValue) {
                    ParameterValue byName = object.parameter(value.getDescriptor().getName().getCode());
                    mandatory("ParameterValueGroup: parameter(String) should returns a value.", byName);
                    if (byName != null) {
                        assertSame("ParameterValueGroup: value(String) inconsistent with values().", value, byName);
                    }
                }
            }
        }
    }
}