/*******************************************************************************
 * Copyright (c) 2008 Martin Lippert and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *   Martin Lippert            initial implementation
 *******************************************************************************/

package org.eclipse.equinox.weaving.hooks;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.equinox.service.weaving.SupplementerRegistry;
import org.eclipse.osgi.framework.adaptor.BundleClassLoader;
import org.eclipse.osgi.framework.adaptor.BundleData;
import org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook;
import org.osgi.framework.Bundle;

/**
 * This class implements the delegate hook for the class loader to allow
 * supplemented bundles find types and resources from theirs supplementer
 * bundles
 * 
 * This works together with the supplementer registry to handle the
 * supplementing mechanism. The supplementer registry controls which bundle is
 * supplemented by which other bundle. This hook implementation uses this
 * information to broaden type and resource visibility according to the
 * supplementer registry information.
 */
public class WeavingLoaderDelegateHook implements ClassLoaderDelegateHook {

    private final SupplementerRegistry supplementerRegistry;

    /**
     * Create the hook instance for broaden the visibility according to the
     * supplementing mechansism.
     * 
     * @param supplementerRegistry The supplementer registry to be used by this
     *            hook for information retrieval which bundles are supplemented
     *            by which other bundles (needs to not be null)
     */
    public WeavingLoaderDelegateHook(
            final SupplementerRegistry supplementerRegistry) {
        this.supplementerRegistry = supplementerRegistry;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#postFindClass(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public Class postFindClass(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws ClassNotFoundException {
        final long bundleID = data.getBundleID();
        final Bundle[] supplementers = supplementerRegistry
                .getSupplementers(bundleID);
        if (supplementers != null) {
            for (int i = 0; i < supplementers.length; i++) {
                try {
                    final Class clazz = supplementers[i].loadClass(name);
                    if (clazz != null) {
                        return clazz;
                    }
                } catch (final ClassNotFoundException e) {
                }
            }
        }

        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#postFindLibrary(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public String postFindLibrary(final String name,
            final BundleClassLoader classLoader, final BundleData data) {
        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#postFindResource(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public URL postFindResource(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws FileNotFoundException {
        final long bundleID = data.getBundleID();
        final Bundle[] supplementers = supplementerRegistry
                .getSupplementers(bundleID);
        if (supplementers != null) {
            for (int i = 0; i < supplementers.length; i++) {
                try {
                    final URL resource = supplementers[i].getResource(name);
                    if (resource != null) {
                        return resource;
                    }
                } catch (final Exception e) {
                }
            }
        }

        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#postFindResources(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public Enumeration postFindResources(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws FileNotFoundException {
        final long bundleID = data.getBundleID();
        final Bundle[] supplementers = supplementerRegistry
                .getSupplementers(bundleID);
        if (supplementers != null) {
            for (int i = 0; i < supplementers.length; i++) {
                try {
                    final Enumeration resource = supplementers[i]
                            .getResources(name);
                    if (resource != null) {
                        // TODO: if more than one enumeration is found, we should return all items
                        return resource;
                    }
                } catch (final Exception e) {
                }
            }
        }

        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#preFindClass(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public Class preFindClass(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws ClassNotFoundException {
        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#preFindLibrary(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public String preFindLibrary(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws FileNotFoundException {
        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#preFindResource(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public URL preFindResource(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws FileNotFoundException {
        return null;
    }

    /**
     * @see org.eclipse.osgi.framework.adaptor.ClassLoaderDelegateHook#preFindResources(java.lang.String,
     *      org.eclipse.osgi.framework.adaptor.BundleClassLoader,
     *      org.eclipse.osgi.framework.adaptor.BundleData)
     */
    public Enumeration preFindResources(final String name,
            final BundleClassLoader classLoader, final BundleData data)
            throws FileNotFoundException {
        return null;
    }

}