/*******************************************************************************
 * Copyright (c) 2020 Red Hat Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Red Hat Inc. - initial version
 *******************************************************************************/
package org.eclipse.equinox.internal.security.linux;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public class GList extends Structure {
	public static class ByReference extends GList implements Structure.ByReference {
		public ByReference(Pointer p) {
			super(p);
			read();
		}
	}
	public GList(Pointer p) {
		super(p);
		read();
	}
	Pointer data;
	GList.ByReference next;
	GList.ByReference prev;
	@Override
	protected List<String> getFieldOrder() {
		return Arrays.asList(new String[] { "data", "next", "prev" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}