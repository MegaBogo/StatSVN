/*
    StatCvs - CVS statistics generation 
    Copyright (C) 2002  Lukasz Pekacki <lukasz@pekacki.de>
    http://statcvs.sf.net/
    
    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Lesser General Public
    License as published by the Free Software Foundation; either
    version 2.1 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with this library; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
    
	$RCSfile: RepositoryFileManager.java,v $ 
	Created on $Date: 2009/08/20 17:44:05 $ 
*/
package net.sf.statcvs.input;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import net.sf.statcvs.util.FileUtils;

/**
 * Manages a checked-out repository and provides access to
 * line number counts for repository files.
 * 
 * @author Manuel Schulze
 * @author Steffen Pingel
 * @version $Id: RepositoryFileManager.java,v 1.27 2009/08/20 17:44:05 benoitx Exp $
 */
public class RepositoryFileManager {
    private final Logger logger;
    private final String path;
    private final Hashtable revByFilename = new Hashtable();

    /**
     * Creates a new instance with root at <code>pathName</code>.
     * 
     * @param pathName the root of the checked out repository
     */
    public RepositoryFileManager(final String pathName) {
        path = pathName;
        logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Returns the lines of code for a repository file.
     * 
     * @param filename a file in the repository
     * @return the lines of code for a repository file
     * @throws NoLineCountException when the line count could not be retrieved,
     * for example when the file was not found.
     */
    public int getLinesOfCode(final String filename) throws NoLineCountException {
        final String absoluteName = FileUtils.getAbsoluteName(this.path, filename);
        try {
            final FileReader freader = new FileReader(absoluteName);
            final BufferedReader reader = new BufferedReader(freader);
            final int linecount = getLineCount(reader);
            logger.finer("line count for '" + absoluteName + "': " + linecount);
            freader.close();
            return linecount;
        } catch (final IOException e) {
            throw new NoLineCountException("could not get line count for '" + absoluteName + "': " + e);
        }
    }

    private int getLineCount(final BufferedReader reader) throws IOException {
        int linecount = 0;
        while (reader.readLine() != null) {
            linecount++;
        }
        return linecount;
    }

    /**
     * Returns the revision of filename in the local working directory by 
     * reading the CVS/Entries file.
     * @param filename the filename
     * @return the revision of filename
     */
    public String getRevision(final String filename) throws IOException {
        String rev = (String) revByFilename.get(filename);
        if (rev != null) {
            return rev;
        }

        final String baseDir = FileUtils.getParentDirectoryPath(filename);
        final String entriesFilename = baseDir + "CVS" + FileUtils.getDefaultDirSeparator() + "Entries";

        // read CVS/Entries file
        final String absoluteName = FileUtils.getAbsoluteName(this.path, entriesFilename);
        final BufferedReader in = new BufferedReader(new FileReader(absoluteName));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("D")) {
                    // ignore, directory entry
                } else {
                    // cache all entries
                    final StringTokenizer t = new StringTokenizer(line, "/");
                    if (t.countTokens() >= 2) {
                        revByFilename.put(baseDir + t.nextToken(), t.nextToken());
                    } else {
                        // invalid entry
                    }
                }
            }

            rev = (String) revByFilename.get(filename);
            if (rev != null) {
                return rev;
            } else {
                throw new IOException("File " + filename + " has no revision");
            }
        } finally {
            in.close();
        }
    }

}
