/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $URL$
 **
 ** Copyright (C) 2003-2005 Open GIS Consortium, Inc.
 ** All Rights Reserved. http://www.opengis.org/legal/
 **
 *************************************************************************************************/
package org.opengis.referencing.datum;

import java.util.Map;
import java.util.Date;
import javax.units.Unit;
import org.opengis.referencing.ObjectFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Builds up complex {@linkplain Datum datums} from simpler objects or values.
 * {@code DatumFactory} allows applications to make {@linkplain Datum datums}
 * that cannot be created by a {@link DatumAuthorityFactory}. This factory is very
 * flexible, whereas the authority factory is easier to use.
 * So {@link DatumAuthorityFactory} can be used to make "standard" datums, and
 * {@code DatumFactory} can be used to make "special" datums.
 *
 * @version <A HREF="http://www.opengis.org/docs/01-009.pdf">Implementation specification 1.0</A>
 * @author Open Geospatial Consortium
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 *
 * @see org.opengis.referencing.cs.CSFactory
 * @see org.opengis.referencing.crs.CRSFactory
 */
@UML(identifier="CS_CoordinateSystemFactory", specification=OGC_01009)
public interface DatumFactory extends ObjectFactory {
    /**
     * Creates an engineering datum.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createLocalDatum", specification=OGC_01009)
    EngineeringDatum createEngineeringDatum(Map<String, ?> properties)
            throws FactoryException;

    /**
     * Creates geodetic datum from ellipsoid and (optionaly) Bursa-Wolf parameters.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  ellipsoid Ellipsoid to use in new geodetic datum.
     * @param  primeMeridian Prime meridian to use in new geodetic datum.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createHorizontalDatum", specification=OGC_01009)
    GeodeticDatum createGeodeticDatum(Map<String, ?> properties,
                                      Ellipsoid      ellipsoid,
                                      PrimeMeridian  primeMeridian) throws FactoryException;

    /**
     * Creates an image datum.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  pixelInCell Specification of the way the image grid is associated
     *         with the image data attributes.
     * @throws FactoryException if the object creation failed.
     */
    ImageDatum createImageDatum(Map<String, ?> properties,
                                PixelInCell    pixelInCell) throws FactoryException;

    /**
     * Creates a temporal datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  origin The date and time origin of this temporal datum.
     * @throws FactoryException if the object creation failed.
     */
    TemporalDatum createTemporalDatum(Map<String, ?> properties,
                                      Date           origin) throws FactoryException;

    /**
     * Creates a vertical datum from an enumerated type value.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  type The type of this vertical datum (often "geoidal").
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createVerticalDatum", specification=OGC_01009)
    VerticalDatum createVerticalDatum(Map<String, ?>    properties,
                                      VerticalDatumType type) throws FactoryException;

    /**
     * Creates an ellipsoid from radius values.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  semiMinorAxis Polar radius in supplied linear units.
     * @param  unit Linear units of ellipsoid axes.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createEllipsoid", specification=OGC_01009)
    Ellipsoid createEllipsoid(Map<String, ?> properties,
                              double         semiMajorAxis,
                              double         semiMinorAxis,
                              Unit           unit) throws FactoryException;

    /**
     * Creates an ellipsoid from an major radius, and inverse flattening.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  semiMajorAxis Equatorial radius in supplied linear units.
     * @param  inverseFlattening Eccentricity of ellipsoid.
     * @param  unit Linear units of major axis.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createFlattenedSphere", specification=OGC_01009)
    Ellipsoid createFlattenedSphere(Map<String, ?> properties,
                                    double         semiMajorAxis,
                                    double         inverseFlattening,
                                    Unit           unit) throws FactoryException;

    /**
     * Creates a prime meridian, relative to Greenwich.
     *
     * @param  properties Name and other properties to give to the new object.
     *         Available properties are {@linkplain ObjectFactory listed there}.
     * @param  longitude Longitude of prime meridian in supplied angular units East of Greenwich.
     * @param  angularUnit Angular units of longitude.
     * @throws FactoryException if the object creation failed.
     */
    @UML(identifier="createPrimeMeridian", specification=OGC_01009)
    PrimeMeridian createPrimeMeridian(Map<String, ?> properties,
                                      double         longitude,
                                      Unit           angularUnit) throws FactoryException;
}
