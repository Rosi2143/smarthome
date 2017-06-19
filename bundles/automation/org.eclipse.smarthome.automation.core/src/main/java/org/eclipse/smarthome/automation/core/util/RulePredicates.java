/**
 * Copyright (c) 2017 by Deutsche Telekom AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.smarthome.automation.Rule;
import org.eclipse.smarthome.automation.core.internal.RuleEngine;
import org.eclipse.smarthome.core.thing.UID;

/**
 * Add support for namespace prefix and provide default predicates for namespace and tags.
 *
 * @author Victor Toni - initial contribution
 *
 */
public class RulePredicates {

    /**
     * Constant defining separator between module UID and output name.
     * 
     * Reuses the value from {@link UID#SEPARATOR}.
     */
    public static final String PREFIX_SEPARATOR = UID.SEPARATOR;

    /**
     * Gets the namespace of the {@link Rule}, if any exist. This property is either set by the {@link RuleEngine} when
     * the {@link Rule} is added or by the creating party. It's an optional property.
     * <br/><br/>Implementation note
     *    <br/>The namespace is part of the {@code UID} and the prefix thereof.
     *    <br/>If the UID does not contain a {@link UID#SEPARATOR} {@code null} will be returned.
     *    <br/>If the UID does contain a {@link UID#SEPARATOR} the prefix until the first occurrence will be returned.
     *    <br/>If the prefix would have a zero length {@code null} will be returned.
     *
     * @return namespace of this {@link Rule}, or {@code null} if no prefix or an empty prefix is found
    */
    public static String getNamespace(Rule rule) {
        if (null != rule) {
            final String uid = rule.getUID();
            if (null != uid) {
                final int index = uid.indexOf(PREFIX_SEPARATOR);

                // only when a delimiter was found and the prefix is not empty
                if (0 < index) {
                    return uid.substring(0, index);
                }
            }
        }

        return null;
    }

    /**
     * Creates a {@link Predicate} which can be used to filter {@link Rule}s for a given namespace or {@code null} namespace.
     *
     * @param namespace to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasNamespace(final String namespace) {
        if (null == namespace) {
            return r -> null == getNamespace(r);
       } else {
            return r-> namespace.equals(getNamespace(r));
        }
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s for any of the given namespaces and even {@code null} namespace.
     *
     * @param namespaces to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAnyOfNamespaces(String... namespaces) {
        final HashSet<String> namespaceSet = new HashSet<String>(namespaces.length);
        for(final String namespace : namespaces) {
            namespaceSet.add(namespace);
        }

        // this will even work for null namepace
        return r -> namespaceSet.contains(getNamespace(r));
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s with one or more tags.
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasTags() {
        // everything with a tag is matching
        // Rule.getTags() is never null
        return r -> 0 < r.getTags().size();
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s without tags.
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasNoTags() {
        // Rule.getTags() is never null
        return r -> r.getTags().isEmpty();
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s with all given tags or no tags at all.
     * All given tags must match, (the matched {@code Rule} might contain more).
     *
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAllTags(final Collection<String> tags) {
        if (null == tags || tags.isEmpty()) {
            return hasNoTags();
        } else {
            final Set<String> tagSet = new HashSet<String>(tags);

            // everything containing _all_ given tags is matching
            // (Rule might might have more tags than the given set)
            return r -> r.getTags().containsAll(tagSet);
        }
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s for all given tags or no tags at all.
     * All given tags must match, (the matched {@code Rule} might contain more).
     *
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAllTags(final String... tags) {
        if (null == tags || 0 == tags.length) {
            return hasNoTags();
        } else {
            return hasAllTags(Arrays.asList(tags));
        }
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s for any of the given tags or {@link Rule}s without tags.
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAnyOfTags(final Collection<String> tags) {
        if (null == tags || tags.isEmpty()) {
            // everything without a tag is matching
            return hasNoTags();
        } else {
            final Set<String> tagSet = new HashSet<String>(tags);

            // everything containing _any_ of the given tags is matching (more than one tag might match)
            // if the collections are NOT disjoint, they have something in common
            return r -> !Collections.disjoint(r.getTags(), tagSet);
        }
    }

    /**
     * Creates a {@link Predicate} which can be used to match {@link Rule}s for any of the given tags or {@link Rule}s without tags.
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAnyOfTags(final String... tags) {
        if (null == tags || 0 == tags.length) {
            // everything without a tag is matching
            return hasNoTags();
        } else {
            return hasAnyOfTags(Arrays.asList(tags));
        }
    }

}
