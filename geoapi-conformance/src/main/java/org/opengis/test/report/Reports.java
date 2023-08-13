/*
 *    GeoAPI - Java interfaces for OGC/ISO standards
 *    Copyright © 2011-2023 Open Geospatial Consortium, Inc.
 *    http://www.geoapi.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.opengis.test.report;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;

import org.opengis.util.Factory;
import org.opengis.util.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.operation.MathTransformFactory;


/**
 * A single point for generating every reports implemented in this package.
 * Usage example:
 *
 * {@snippet lang="java" :
 * Properties props = new Properties();
 * props.setProperty("PRODUCT.NAME", "MyProduct");
 * props.setProperty("PRODUCT.URL",  "http://www.myproject.org");
 * Reports reports = new Reports(props);
 * reports.add(MathTransformFactory.class, myTransformFactory);
 * reports.write(new File("my-output-directory"));
 * }
 *
 * @author Martin Desruisseaux (Geomatys)
 * @version 3.1
 *
 * @since 3.1
 */
public class Reports extends Report {
    /**
     * The report generators.
     */
    private final List<Report> reports;

    /**
     * Instances automatically generated.
     * Those instances are remembered for making possible to append new factories to them.
     */
    private final Map<Class<? extends Report>, Report> instances;

    /**
     * The table of contents, as (title, URL) pairs.
     */
    private final Map<String,File> contents;

    /**
     * Creates a new report generator using the given property values.
     * See the {@link Report} Javadoc for a list of expected values.
     *
     * @param properties  the property values, or {@code null} for the default values.
     */
    public Reports(final Properties properties) {
        super(properties);
        reports   = new ArrayList<>();
        instances = new HashMap<>();
        contents  = new LinkedHashMap<>();
    }

    /**
     * Add a report for the given factory.
     * The current implementation can handle the following kind of factories:
     *
     * <ul>
     *   <li>{@link CRSAuthorityFactory},  given to {@link AuthorityCodesReport}</li>
     *   <li>{@link MathTransformFactory}, given to {@link OperationParametersReport}</li>
     * </ul>
     *
     * @param  <T>      compile-time value of the {@code type} argument.
     * @param  type     the factory type, specified in case the factory implements many interfaces.
     * @param  factory  the factory for which to generate a report.
     * @return {@code true} if this method will generate a report for the given factory,
     *         or {@code false} if the factory has been ignored.
     * @throws FactoryException if an error occurred while querying the factory.
     */
    public <T extends Factory> boolean add(final Class<T> type, final T factory) throws FactoryException {
        boolean modified = false;
        if (CRSAuthorityFactory.class.isAssignableFrom(type)) {
            final AuthorityCodesReport report = getReport(AuthorityCodesReport.class);
            if (report != null) {
                report.add((CRSAuthorityFactory) factory);
                modified = true;
            }
        }
        if (MathTransformFactory.class.isAssignableFrom(type)) {
            final OperationParametersReport report = getReport(OperationParametersReport.class);
            if (report != null) {
                report.add((MathTransformFactory) factory);
                modified = true;
            }
        }
        return modified;
    }

    /**
     * Returns a report of the given type. If a report has already been created by a previous
     * invocation of this method. Then that report is returned. Otherwise a new report is
     * created and cached for appending.
     *
     * @param  <T>   the compile-time type of the {@code type} argument.
     * @param  type  the kind of report to create.
     * @return the report of the given type, or {@code null} if no report of the given type should be generated.
     * @throws IllegalArgumentException if the given type is not a report that can be instantiated.
     */
    private <T extends Report> T getReport(final Class<T> type) throws IllegalArgumentException {
        final Report candidate = instances.get(type);
        if (candidate != null) {
            return type.cast(candidate);
        }
        final T report = createReport(type);
        if (report != null) {
            if (reports.isEmpty()) {
                /*
                 * If we are creating the first report, creates the initial listener which will
                 * delegate the calls to 'ProgressListener.progress(int,int)' to the 'progress'
                 * method in this class. All other listeners will be chained before this one.
                 *
                 * We need to wrap the "delegator" listener into another listener in order to
                 * allow all future listeners to be inserted between the two: the "delegator"
                 * listener must always be last, and the listener associated to the first report
                 * must stay first.
                 */
                report.listener = new ProgressListener(new ProgressListener(null, false) {
                    @Override void progress(final int position, final int count) {
                        Reports.this.progress(position, count);
                    }
                }, false);
            } else {
                report.listener = new ProgressListener(reports.get(0).listener, true);
            }
            instances.put(type, report);
            reports.add(report);
        }
        return report;
    }

    /**
     * Invoked when {@code Reports} need to create a new instance of the given class.
     * Subclasses can override this method in order to customize their {@code Report}
     * instances.
     *
     * <p>The default implementation creates a new instance of the given classes using
     * the {@linkplain java.lang.reflect.Constructor#newInstance(Object[]) reflection API}.
     * The given type shall declare a public constructor expecting a single {@link Properties}
     * argument.</p>
     *
     * @param  <T>   the compile-time type of the {@code type} argument.
     * @param  type  the kind of report to create.
     * @return the report of the given type, or {@code null} if no report of the given type should be generated.
     * @throws IllegalArgumentException if the given type is not a kind of report that this method can instantiate.
     */
    protected <T extends Report> T createReport(final Class<T> type) throws IllegalArgumentException {
        try {
            return type.cast(type.getConstructor(Properties.class).newInstance(properties));
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Cannot instantiate report of type " + type.getSimpleName(), e);
        }
    }

    /**
     * Writes in the given directory every reports added to this {@code Reports} instance.
     *
     * @param  directory  the directory where to write the reports.
     * @return the index file, or the main file in only one report has been created,
     *         or {@code null} if no report has been created.
     * @throws IOException if an error occurred while writing a report.
     */
    @Override
    public File write(final File directory) throws IOException {
        File main = null;
        boolean needsTOC = false;
        for (final Report report : reports) {
            File file = report.toFile(directory);
            file = report.write(file);
            final String title = report.getProperty("TITLE").trim();
            if (!title.isEmpty()) {
                if (contents.put(title, relativize(directory, file)) != null) {
                    throw new IOException("Duplicated title: " + title);
                }
            }
            if (file != null) {
                if (main == null) {
                    main = file;
                } else if (!needsTOC) {
                    main = new File(directory, "index.html");
                    needsTOC = true;
                }
            }
        }
        if (needsTOC) {
            filter("index.html", main);
        }
        return main;
    }

    /**
     * Invoked by {@link Report} every time a {@code ${FOO}} occurrence is found.
     * This method generates the table of content.
     */
    @Override
    final void writeContent(final BufferedWriter out, final String key) throws IOException {
        if (!"CONTENT".equals(key)) {
            super.writeContent(out, key);
            return;
        }
        for (final Map.Entry<String,File> entry : contents.entrySet()) {
            writeIndentation(out, 8);
            out.write("<li><a href=\"");
            out.write(escape(entry.getValue().getPath().replace(File.separatorChar, '/')));
            out.write("\">");
            out.write(entry.getKey());                                  // Already escaped.
            out.write("</a></li>");
            out.newLine();
        }
    }

    /**
     * Returns a file relative to the given directory.
     *
     * @param  directory  the directory of the file.
     * @param  file       file relative to the given directory.
     * @return file in the given directory.
     */
    private static File relativize(final File directory, File file) {
        File relative = new File(file.getName());
        while ((file = file.getParentFile()) != null) {
            if (file.equals(directory)) {
                break;
            }
            relative = new File(file.getName(), relative.getPath());
        }
        return relative;
    }
}
