/**
 * WebLogic Automation Book Source Code (JMX sources)
 * 
 * This file is part of the WLS-Automation book sourcecode software distribution. 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Martin Heinzl
 * Copyright (C) 2013 MH-EnterpriseConsulting, All rights reserved.
 *
 */
package com.wlsautomation.utils;

import java.io.*;
import java.util.*;



/**
 *
 * <p>Title: File Utils</p>
 *
 * <p>Description: </p>
 * A set of different common file utils.

 *
 * @author Martin Heinzl
 * @version 1.0
 */
public class FileUtils
{

    /**
     * Check if this file/directory exist
     * @param p_filename String
     * @return boolean
     */
    public static boolean fileExist(String p_filename)
    {
        return (new File(p_filename)).exists();
    }


    /**
     * Delete the given file
     *
     * @param p_filename String
     * @return boolean
     */
    public static boolean deleteFile(String p_filename)
    {
        return (new File(p_filename)).delete();
    }


    /**
     * Is the given name a directory ?
     * @param p_path String
     * @return boolean
     */
    public static boolean isDirectory(String p_path)
    {
        File f = new File(p_path);
        return f.isDirectory();
    }


    /**
     * Is the given name a file ?
     * @param p_path String
     * @return boolean
     */
    public static boolean isFile(String p_path)
    {
        File f = new File(p_path);
        return f.isFile();
    }


    /**
     * Create directory and all necessary parent directories
     * @param p_path String
     * @return boolean
     */
    public static boolean createSubDirectories(String p_path)
    {
        return createSubDirectories(p_path, false);
    }


    /**
     * Create directory and all necessary parent directories. If isfile=true,
     * then treat the last element as a filename and do not create the leaf element.
     *
     * @param p_path String
     * @param p_isfile boolean
     * @return boolean
     */
    public static boolean createSubDirectories(String p_path, boolean p_isfile)
    {
        if (p_isfile)
        {
            p_path = getDirectory(p_path, "/");
        }
        File f = new File(p_path);
        if (!f.exists())
        {
            return f.mkdirs();
        }
        else
        {
            return true;
        }
    }


    /**
     * Get the directory
     * @param p_filename String
     * @param p_separator String
     * @return String
     */
    public static String getDirectory(String p_filename, String p_separator)
    {
        int index = p_filename.lastIndexOf(p_separator);
        if (index == -1)
        {
            return "";
        }
        else
        {
            return p_filename.substring(0, index);
        }
    }


    /**
     * Delete temp files
     * @param p_dir String
     */
    protected static void cleanupTemp(String p_dir)
    {
        File f = new File(p_dir);
        if (!f.exists())
        {
            return;
        }
        if (f.isDirectory())
        {
            recursiveDelete(p_dir, true);
        }
        else
        {
            f.delete();
        }
    }


    /**
     * Recursively delete a directory.  BE CAREFUL, this deletes everything
     * @param p_path String
     * @param p_deletemetoo boolean
     * @return boolean
     */
    public static boolean recursiveDelete(String p_path, boolean p_deletemetoo)
    {
         if (p_path == null)
        {
            return true;
        }
        String path = p_path;
        if (path.endsWith("\\") || path.endsWith("/"))
        {
            path = path.substring(0, path.length() - 1);
        }
        File f = new File(path);
        if (f.isFile())
        {
            return p_deletemetoo ? f.delete() : true;
        }
        String contents[] = f.list();
        int len = contents != null ? contents.length : 0;
        boolean result = true;
        for (int i = 0; i < len; i++)
        {
            if (!recursiveDelete(path + File.separator + contents[i], true))
            {
                result = false;
            }
        }

        if (p_deletemetoo && !f.delete())
        {
            result = false;
        }
        return result;
    }


    /**
     * Read the content of a TEXT file
     *
     * @param p_filename String
     * @return String
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String readFile(String p_filename) throws IOException, FileNotFoundException
    {
        int len = (int) (new File(p_filename)).length();
        StringBuffer buf = new StringBuffer(len);
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(p_filename)));
        try
        {
            do
            {
                String data = r.readLine();
                if (data == null)
                {
                    break;
                }
                buf.append(data);
                buf.append('\n');
            }
            while (true);
        }
        finally
        {
            r.close();
        }
        return buf.toString();
    }


    /**
     * Read the content of a TEXT file
     *
     * @param p_filename String
     * @return String
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static ArrayList<String> readFileAsListOfLines(String p_filename) throws IOException, FileNotFoundException
    {
        ArrayList<String> result = new ArrayList<String>();
        BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(p_filename)));
        try
        {
            do
            {
                String data = r.readLine();
                if (data == null)
                {
                    break;
                }
                result.add(data);
            }
            while (true);
        }
        finally
        {
            r.close();
        }
        return result;
    }



    /**
     * Read a file from a resource rather than from a file
     *
     * @param filePath      name of file to open. The file can reside
     *                      anywhere in the classpath
     */
    public static String readFileFromResource(@SuppressWarnings("rawtypes") Class refClass, String filePath) throws java.io.IOException
    {
        StringBuffer fileData = new StringBuffer(1000);

        BufferedReader reader = new BufferedReader(new InputStreamReader(refClass.getResourceAsStream(filePath)));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    /**
     * Write a TEXT file
     * @param p_filename String
     * @param p_buffer String
     * @throws IOException
     */
    public static void writeFile(String p_filename, String p_buffer) throws IOException
    {
        FileOutputStream os = null;
        try
        {
            os = new FileOutputStream(p_filename);
            if (p_buffer != null)
            {
                os.write(p_buffer.getBytes());
            }
            os.close();
            os = null;
        }
        finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }


    /**
     * Write a binary file
     *
     * @param p_filename String
     * @param p_buffer byte[]
     * @throws IOException
     */
    public static void writeBINFile(String p_filename, byte[] p_buffer) throws IOException
    {
        FileOutputStream os = null;
        try
        {
            os = new FileOutputStream(p_filename);
            if (p_buffer != null)
            {
                os.write(p_buffer);
            }
            os.close();
            os = null;
        }
        finally
        {
            if (os != null)
            {
                os.close();
            }
        }
    }


    /**
     * Read a binary file
     *
     * @param p_filename String
     * @return byte[]
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static byte[] readBINFile(String p_filename) throws IOException, FileNotFoundException
    {
        int len = (int) (new File(p_filename)).length();
        byte[] result = new byte[len];

        FileInputStream fis = new FileInputStream(p_filename);

        fis.read(result);

        return result;
    }


    /**
     * Create a directory
     * @param directory String
     * @return boolean
     */
    public static boolean createDirectory(String directory)
    {
        File f = new File(directory);

        if (!f.exists())
        {
            return f.mkdirs();
        }
        else
        {
            // already exists, therefore true
            return true;
        }
    }


    /**
     * Get a list of subdirectories (as strings) for a given directory
     *
     * @param rootDir String
     * @param directory boolean
     * @param sort boolean
     * @return ArrayList
     */
    public static ArrayList<String> getDirectoryList(String rootDir, boolean directory, boolean sort)
    {
        ArrayList<String> result = new ArrayList<String>();
        if (rootDir == null)
        {
            return result;
        }

        File f = new File(rootDir);

        if (!f.exists())
        {
            return result;
        }

        if (f.isFile())
        {
            return result;
        }

        String contents[] = f.list();
        int len = contents != null ? contents.length : 0;

        for (int i = 0; i < len; i++)
        {
            File tmp = new File(rootDir + File.separator + contents[i]);
            if (tmp.isDirectory() == directory)
            {
                result.add(contents[i]);
            }
        }

        if (sort)
        {
            Collections.sort(result);
        }

        return result;
    }


    /**
     * Get a list of element (files and subdirs) (as strings) for a given directory
     *
     * @param rootDir String
     * @param directory boolean
     * @param sort boolean
     * @return ArrayList
     */
    public static ArrayList<String> getDirectoryCompleteContentList(String rootDir, boolean includeRootInResult)
    {
        ArrayList<String> result = new ArrayList<String>();
        if (rootDir == null)
        {
            return result;
        }

        File f = new File(rootDir);

        if (!f.exists())
        {
            return result;
        }

        if (f.isFile())
        {
            return result;
        }

        String contents[] = f.list();
        int len = contents != null ? contents.length : 0;

        for (int i = 0; i < len; i++)
        {
            if (includeRootInResult)
                result.add(rootDir + File.separator + contents[i]);
            else
                result.add(contents[i]);
        }

        return result;
    }


    /**
     * Copy a given file
     *
     * @param fromFileName String
     * @param toFileName String
     * @throws IOException
     */
    public static void copy(String fromFileName, String toFileName) throws IOException
    {
        File fromFile = new File(fromFileName);
        File toFile = new File(toFileName);

        if (!fromFile.exists())
        {
            throw new IOException("FileCopy: " + "no such source file: "
                                  + fromFileName);
        }
        if (!fromFile.isFile())
        {
            throw new IOException("FileCopy: " + "can't copy directory: "
                                  + fromFileName);
        }
        if (!fromFile.canRead())
        {
            throw new IOException("FileCopy: " + "source file is unreadable: "
                                  + fromFileName);
        }

        if (toFile.isDirectory())
        {
            toFile = new File(toFile, fromFile.getName());
        }

        String parent = toFile.getParent();
        if (parent == null)
        {
            parent = System.getProperty("user.dir");
        }
        File dir = new File(parent);
        if (!dir.exists())
        {
            throw new IOException("FileCopy: "
                                  + "destination directory doesn't exist: " + parent);
        }
        if (dir.isFile())
        {
            throw new IOException("FileCopy: "
                                  + "destination is not a directory: " + parent);
        }
        if (!dir.canWrite())
        {
            throw new IOException("FileCopy: "
                                  + "destination directory is unwriteable: " + parent);
        }
        FileInputStream from = null;
        FileOutputStream to = null;
        try
        {
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = from.read(buffer)) != -1)
            {
                to.write(buffer, 0, bytesRead); // write
            }
        }
        finally
        {
            if (from != null)
            {
                try
                {
                    from.close();
                }
                catch (IOException e)
                {
                    ;
                }
            }
            if (to != null)
            {
                try
                {
                    to.close();
                }
                catch (IOException e)
                {
                    ;
                }
            }
        }

    }

}
