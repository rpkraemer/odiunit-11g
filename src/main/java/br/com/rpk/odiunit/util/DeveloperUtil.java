/*
 * Copyright 2012 Robson Kraemer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.rpk.odiunit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class with some util methods to be used
 * 
 * @author robsonpk
 *
 */
abstract public class DeveloperUtil {
	
	public static class ConnectionUtil {
		
		private static Map<String, Connection> databaseConnections;
		static {databaseConnections = new HashMap<String, Connection>();}
		
		/**
		 * Add a database connection object to the collection
		 * @param connectionName - connection name to be used as "key"
		 * @param connection - the Connection object itself
		 */
		public static void addDatabaseConnection(String connectionName, Connection connection) {
			databaseConnections.put(connectionName, connection);
		}

		/**
		 * Recuperate a Database Connection
		 * @param connectionName - the connection name (key) of connection
		 * @return
		 */
		public static Connection getDatabaseConnection(String connectionName) {
			Connection databaseConnection = databaseConnections.get(connectionName);
			if (databaseConnection == null)
				throw new IllegalArgumentException("There is no connection for " + connectionName);
			return databaseConnection;
		}
	}

	public static class FileUtil {
		/**
		 * Delete one directory and you sub-dirs
		 * @param directory
		 */
		public static void deleteDirectory(File directory) {
			if (directory.isDirectory())
				for (File file : directory.listFiles())
					deleteDirectory(file);
			directory.delete();
		}
		
		/**
		 * Count all occurrences of file with a filename pattern name in a directory
		 * optionally can search in sub-directories
		 * 
		 * @param directory - directory to be searched
		 * @param regexFilterPattern - regex filename pattern - to find ALL type <b>.*</b>
		 * @param includeSubDirs - true to include sub-dirs search, false otherwise
		 * @return
		 */
		public static int countFilesOnDirectory(File directory, String regexFilterPattern, boolean includeSubDirs) {
			if (!directory.exists())
				throw new IllegalArgumentException("The directory does not exist");
			if (!directory.isDirectory())
				throw new IllegalArgumentException("The directory passed is not a valid directory");

			int qtdDirectoryFiles = 0;
			File[] directoryFiles = directory.listFiles();
			Pattern pattern = Pattern.compile(regexFilterPattern);
			
			for (File dirOrFile : directoryFiles) {
				Matcher matcher = pattern.matcher(dirOrFile.getName());
				if (dirOrFile.isFile() && matcher.matches())
					qtdDirectoryFiles++;
				else if (dirOrFile.isDirectory() && includeSubDirs)
					qtdDirectoryFiles += countFilesOnDirectory(dirOrFile, regexFilterPattern, includeSubDirs);
			}

			return qtdDirectoryFiles;
		}
		
		/**
		 * Move file to another directory maintaining the same name
		 * @param source - file to be moved
		 * @param targetDirectory - target directory
		 */
		public static void moveFile(File source, File targetDirectory) {
			// move file maintaining the same name
			moveCopyFile(source, targetDirectory, new File(source.getName()), true);
		}
		
		/**
		 * Move file to another directory but with other name
		 * @param source - file to be moved
		 * @param targetDirectory - target directory
		 * @param targetFile - target file (name + extension)
		 */
		public static void moveFile(File source, File targetDirectory, File targetFile) {
			moveCopyFile(source, targetDirectory, targetFile, true);
		}
		
		/**
		 * Copy file to another directory maintaining the same name
		 * @param source
		 * @param targetDirectory
		 */
		public static void copyFile(File source, File targetDirectory) {
			moveCopyFile(source, targetDirectory, new File(source.getName()), false);
		}
		
		/**
		 * Copy file to another directory but with other name
		 * @param source
		 * @param targetDirectory
		 * @param targetFile
		 */
		public static void copyFile(File source, File targetDirectory, File targetFile) {
			moveCopyFile(source, targetDirectory, targetFile, false);
		}
	
		/**
		 * Search at begin of each line of file if matches with given pattern
		 * @param pattern - pattern to be searched on each start line
		 * @param file - file to searched/read
		 * @return number of occurrences of pattern
		 */
		public static int fileLineStartPatternOccurrences(String pattern, File file) {
			FileInputStream inputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader reader = null;
			int patternOccurrences = 0;
			Pattern p = createPatternForRegexPattern(pattern);
			try {
				inputStream = new FileInputStream(file);
				inputStreamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
				reader = new BufferedReader(inputStreamReader);
				String line;
				while ((line = reader.readLine()) != null) {
					Matcher matcher = p.matcher(line);
					if (matcher.matches())
						patternOccurrences++;
				}
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
			} finally {
				try {
					inputStream.close();
					inputStreamReader.close();
					reader.close();
				} catch (IOException e) {}
			}
			return patternOccurrences;
		}
		
		/**
		 * Copy all files (optionally files in sub-directories) to a target directory
		 * @param sourceDir - source directory which files must be copied
		 * @param targetDir - target directory
		 * @param includeSubDirsFiles - flag to include or not files of source sub-directories
		 */
		public static void copyFiles(File sourceDir, File targetDir, boolean includeSubDirsFiles) {
			if (!sourceDir.exists())
				throw new IllegalArgumentException("Source directory does not exist");
			if (!sourceDir.isDirectory())
				throw new IllegalArgumentException("Source directory is not a real directory");

			File[] sourceFilesOrDirs = sourceDir.listFiles();
			for (File fileOrDirToBeCopied : sourceFilesOrDirs) {
				if (fileOrDirToBeCopied.isFile())
					copyFile(fileOrDirToBeCopied, targetDir);
				else if (fileOrDirToBeCopied.isDirectory() && includeSubDirsFiles)
					copyFiles(fileOrDirToBeCopied, targetDir, includeSubDirsFiles);
			}
		}
		
		/**
		 * Move all files (optionally files in sub-directories) to a target directory
		 * @param sourceDir - source directory which files must be moved
		 * @param targetDir - target directory
		 * @param includeSubDirsFiles - flag to include or not files of source sub-directories
		 */
		public static void moveFiles(File sourceDir, File targetDir, boolean includeSubDirsFiles) {
			if (!sourceDir.exists())
				throw new IllegalArgumentException("Source directory does not exist");
			if (!sourceDir.isDirectory())
				throw new IllegalArgumentException("Source directory is not a real directory");
			
			File[] sourceFilesOrDirs = sourceDir.listFiles();
			for (File fileOrDirToBeCopied : sourceFilesOrDirs) {
				if (fileOrDirToBeCopied.isFile())
					moveFile(fileOrDirToBeCopied, targetDir);
				else if (fileOrDirToBeCopied.isDirectory() && includeSubDirsFiles)
					moveFiles(fileOrDirToBeCopied, targetDir, includeSubDirsFiles);
			}
		}
	
		private static Pattern createPatternForRegexPattern(String regexPattern) {
			String regex = String.format("^(%s)(.)*", regexPattern);
			Pattern pattern = Pattern.compile(regex);
			return pattern;
		}
		
		/**
		 * INTERNAL ONLY
		 * 
		 * Method that can MOVE or COPY a file
		 * 
		 * @param source
		 * @param targetDirectory
		 * @param targetFile
		 * @param deleteSource
		 */
		private static void moveCopyFile(File source, File targetDirectory, File targetFile, boolean deleteSourceFile) {
			if (!source.exists())
				throw new IllegalArgumentException("The source file does not exist");	
			
			FileInputStream inputStream = null; 
			FileOutputStream outputStream = null;
			FileChannel inputChannel = null, outputChannel = null;
			
			try {
				inputStream = new FileInputStream(source);
				if (!targetDirectory.exists())
					targetDirectory.mkdirs();
				outputStream = new FileOutputStream(new File(targetDirectory.getAbsolutePath(), targetFile.getName()));
				inputChannel = inputStream.getChannel();
				outputChannel = outputStream.getChannel();
				inputChannel.transferTo(0, inputChannel.size(), outputChannel);
			} catch (IOException e) {
				throw new RuntimeException("Cannot move the file: " + e.getMessage());
			} finally {
				try {
					inputChannel.close();
					outputChannel.close();
					inputStream.close();
					outputStream.close();
					
					// delete source file if necessary
					if (deleteSourceFile)
						source.delete();
				} catch (IOException e) {}
			}
		}
	}
}
