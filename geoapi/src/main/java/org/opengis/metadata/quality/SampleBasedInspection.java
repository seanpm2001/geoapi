/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    http://www.geoapi.org
 *
 *    Copyright (C) 2021 Open Geospatial Consortium, Inc.
 *    All Rights Reserved. http://www.opengeospatial.org/ogc/legal
 *
 *    Permission to use, copy, and modify this software and its documentation, with
 *    or without modification, for any purpose and without fee or royalty is hereby
 *    granted, provided that you include the following on ALL copies of the software
 *    and documentation or portions thereof, including modifications, that you make:
 *
 *    1. The full text of this NOTICE in a location viewable to users of the
 *       redistributed or derivative work.
 *    2. Notice of any changes or modifications to the OGC files, including the
 *       date changes were made.
 *
 *    THIS SOFTWARE AND DOCUMENTATION IS PROVIDED "AS IS," AND COPYRIGHT HOLDERS MAKE
 *    NO REPRESENTATIONS OR WARRANTIES, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *    TO, WARRANTIES OF MERCHANTABILITY OR FITNESS FOR ANY PARTICULAR PURPOSE OR THAT
 *    THE USE OF THE SOFTWARE OR DOCUMENTATION WILL NOT INFRINGE ANY THIRD PARTY
 *    PATENTS, COPYRIGHTS, TRADEMARKS OR OTHER RIGHTS.
 *
 *    COPYRIGHT HOLDERS WILL NOT BE LIABLE FOR ANY DIRECT, INDIRECT, SPECIAL OR
 *    CONSEQUENTIAL DAMAGES ARISING OUT OF ANY USE OF THE SOFTWARE OR DOCUMENTATION.
 *
 *    The name and trademarks of copyright holders may NOT be used in advertising or
 *    publicity pertaining to the software without specific, written prior permission.
 *    Title to copyright in this software and any associated documentation will at all
 *    times remain with copyright holders.
 */
package org.opengis.metadata.quality;

import org.opengis.util.InternationalString;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * Test performed on a subset of the geographic data defined by the data quality scope.
 *
 * @author  Alexis Gaillard (Geomatys)
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 *
 * @see FullInspection
 * @see IndirectEvaluation
 *
 * @since 3.1
 */
@UML(identifier="DQ_SampleBasedInspection", specification=ISO_19157)
public interface SampleBasedInspection extends DataEvaluation {
    /**
     * Information of the type of sampling scheme and description of the sampling procedure.
     *
     * @return sampling scheme and sampling procedure.
     */
    @UML(identifier="samplingScheme", obligation=MANDATORY, specification=ISO_19157)
    InternationalString getSamplingScheme();

    /**
     * Information of how lots are defined.
     *
     * @return information on lots.
     */
    @UML(identifier="lotDescription", obligation=MANDATORY, specification=ISO_19157)
    InternationalString getLotDescription();

    /**
     * Information on how many samples on average are extracted for inspection from each lot of population.
     *
     * @return average number of samples extracted for inspection.
     */
    @UML(identifier="sampleRatio", obligation=MANDATORY, specification=ISO_19157)
    InternationalString getSampleRatio();
}
