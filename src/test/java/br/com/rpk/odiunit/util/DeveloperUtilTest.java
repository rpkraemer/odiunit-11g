package br.com.rpk.odiunit.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Random;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.rpk.odiunit.util.DeveloperUtil;

public class DeveloperUtilTest {

	private Mockery mockery;
	
	@Before
	public void setUp() {
		SetUp.createTestFile();
		mockery = new Mockery();
	}
	
	@After
	public void tearDown() {
		SetUp.removeDirs();
		SetUp.removeTestsFile();
	}
	
	/* ************************** FileUtil ************************** */
	
	@Test
	public void shouldCopyOneFileToAnotherDirectoryWithOtherName() {
		File fileToBeCopied = givenAFile();
		whenCopyTheFileToAnotherDirectoryWithOtherName(fileToBeCopied);
		itMustContinueToExistAtSourceDirectory(fileToBeCopied);
		File fileCopied = recuperateCopiedFileWithOtherName();
		Assert.assertTrue(fileCopied.exists());
		Assert.assertTrue(fileToBeCopied.length() == fileCopied.length());
	}
	
	@Test
	public void shouldCopyOneFileToAnotherDirectoryWithSameName() {
		File fileToBeCopied = givenAFile();
		whenCopyTheFileToAnotherDirectory(fileToBeCopied);
		itMustContinueToExistAtSourceDirectory(fileToBeCopied);
		File fileCopied = recuperateCopiedFile();
		Assert.assertTrue(fileCopied.exists());
		Assert.assertTrue(fileToBeCopied.length() == fileCopied.length());
	}
	
	@Test
	public void shouldMoveOneFileToAnotherDirectoryWithOtherName() {
		File fileToMove = givenAFile();
		whenMoveTheFileToAnotherDirectoryWithOtherName(fileToMove);
		itCannotExistAnymoreAtSourceDirectory(fileToMove);
		File fileMoved = recuperateMovedFileWithOtherName();
		Assert.assertTrue(fileMoved.exists());
	}
	
	@Test
	public void shouldMoveOneFileToAnotherDirectoryWithSameName() {
		File fileToBeMoved = givenAFile();
		whenMoveTheFileToAnotherDirectory(fileToBeMoved);
		itCannotExistAnymoreAtSourceDirectory(fileToBeMoved);
		File fileMoved = recuperateMovedFile();
		Assert.assertTrue(fileMoved.exists());
	}
	
	@Test
	public void shouldReturnNumberOfLineStartPatternOccurrencesInFile() {
		File fileToBeSearched = givenAFileToSeachLines();
		int numberOfOccurences = whenReadFileShouldReturnFiveOcurrences(fileToBeSearched);
		Assert.assertEquals(5, numberOfOccurences);
	}
	
	@Test
	public void shouldDeleteDirectory() {
		File fileTests = new File("src/test/resources", "tests.txt");
		File directoryToBeRemoved = new File("src/test/resources/dir");
		DeveloperUtil.FileUtil.copyFile(fileTests, directoryToBeRemoved);
		Assert.assertTrue(directoryToBeRemoved.exists());
		DeveloperUtil.FileUtil.deleteDirectory(directoryToBeRemoved);
		Assert.assertFalse(directoryToBeRemoved.exists());
	}
	
	@Test
	public void shouldDeleteDirectoryWithSubDirectories() {
		File fileTests = new File("src/test/resources", "tests.txt");
		File directoryToBeRemoved = new File("src/test/resources/dir");
		File subDirectoryToBeRemoved = new File("src/test/resources/dir/subdir");
		
		DeveloperUtil.FileUtil.copyFile(fileTests, directoryToBeRemoved);
		DeveloperUtil.FileUtil.copyFile(fileTests, subDirectoryToBeRemoved);
		Assert.assertTrue(directoryToBeRemoved.exists());
		Assert.assertTrue(subDirectoryToBeRemoved.exists());
		
		DeveloperUtil.FileUtil.deleteDirectory(directoryToBeRemoved);
		DeveloperUtil.FileUtil.deleteDirectory(subDirectoryToBeRemoved);
		Assert.assertFalse(directoryToBeRemoved.exists());
		Assert.assertFalse(subDirectoryToBeRemoved.exists());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRiseExceptionWhenTryToMoveAndInexistentSource() {
		File inexistentSourceFile = new File("path/does/not/exist/file.txt");
		File targetDirectory = new File("src/test/resources/dir");
		DeveloperUtil.FileUtil.moveFile(inexistentSourceFile, targetDirectory);
	}
	
	@Test
	public void shouldCountFilesOnADirectory() {
		File directory = new File("src/test/resources/dir");
		directory.mkdirs();
		add10FilesToDirectory(directory);
		Assert.assertEquals(10, DeveloperUtil.FileUtil.countFilesOnDirectory(directory, ".*", false));
	}
	
	@Test
	public void shouldCountFilesOnADirectoryAndSubDirectories() {
		File directory = new File("src/test/resources/dir");
		File subDirectory = new File("src/test/resources/dir/subdir");
		directory.mkdirs();
		subDirectory.mkdirs();
		add10FilesToDirectory(directory);
		add10FilesToDirectory(subDirectory);
		Assert.assertEquals(20, DeveloperUtil.FileUtil.countFilesOnDirectory(directory, ".*", true));
	}
	
	@Test
	public void shouldCountFilesOnADirectoryGivenAPattern() {
		File directory = new File("src/test/resources/dir");
		File subDirectory = new File("src/test/resources/dir/subdir");
		directory.mkdirs();
		subDirectory.mkdirs();
		add10FilesToDirectory(directory);
		add10FilesToDirectory(subDirectory);
		Assert.assertEquals(2, DeveloperUtil.FileUtil.countFilesOnDirectory(directory, "^(.)*6.txt$", true));
	}
	
	@Test
	public void shouldCopyAllFilesOfADirectoryToAnotherDirectory() {
		File sourceDirectory = new File("src/test/resources/dir");
		File targetDirectory = new File("src/test/resources/dir/subdir");
		sourceDirectory.mkdirs();
		add10FilesToDirectory(sourceDirectory);
		DeveloperUtil.FileUtil.copyFiles(sourceDirectory, targetDirectory, false);
		Assert.assertEquals(10, DeveloperUtil.FileUtil.countFilesOnDirectory(targetDirectory, ".*", false));
		//Removing target directory
		DeveloperUtil.FileUtil.deleteDirectory(targetDirectory);
		Assert.assertFalse(targetDirectory.exists());
	}
	
	@Test
	public void shouldCopyAllFilesOfADirectoryAndSubDirectoryToAnotherDirectory() {
		File sourceDirectory = new File("src/test/resources/dir");
		File sourceSubDirectory = new File(sourceDirectory, "subdir");
		File targetDirectory = new File("src/test/resources/target");
		sourceDirectory.mkdirs();
		sourceSubDirectory.mkdirs();
		add10FilesToDirectory(sourceDirectory); 
		addFilesToDirectory(5, sourceSubDirectory);
		DeveloperUtil.FileUtil.copyFiles(sourceDirectory, targetDirectory, true);
		Assert.assertEquals(15, DeveloperUtil.FileUtil.countFilesOnDirectory(targetDirectory, ".*", false));
		//Removing target directory
		DeveloperUtil.FileUtil.deleteDirectory(targetDirectory);
		Assert.assertFalse(targetDirectory.exists());
	}
	
	@Test
	public void shouldMoveAllFilesOfADirectoryToAnotherDirectory() {
		File sourceDirectory = new File("src/test/resources/dir");
		File targetDirectory = new File("src/test/resources/dir/subdir");
		sourceDirectory.mkdirs();
		add10FilesToDirectory(sourceDirectory);
		DeveloperUtil.FileUtil.moveFiles(sourceDirectory, targetDirectory, false);
		Assert.assertEquals(10, DeveloperUtil.FileUtil.countFilesOnDirectory(targetDirectory, ".*", false));
		//Removing target directory
		DeveloperUtil.FileUtil.deleteDirectory(targetDirectory);
		Assert.assertFalse(targetDirectory.exists());
		Assert.assertTrue(sourceDirectory.listFiles().length == 0); //source dir must be empty
	}
	
	@Test
	public void shouldMoveAllFilesOfADirectoryAndSubDirectoryToAnotherDirectory() {
		File sourceDirectory = new File("src/test/resources/dir");
		File sourceSubDirectory = new File(sourceDirectory, "subdir");
		File targetDirectory = new File("src/test/resources/target");
		sourceDirectory.mkdirs();
		sourceSubDirectory.mkdirs();
		add10FilesToDirectory(sourceDirectory); 
		addFilesToDirectory(15, sourceSubDirectory);
		DeveloperUtil.FileUtil.moveFiles(sourceDirectory, targetDirectory, true);
		Assert.assertEquals(25, DeveloperUtil.FileUtil.countFilesOnDirectory(targetDirectory, ".*", false));
		//Removing target directory
		DeveloperUtil.FileUtil.deleteDirectory(targetDirectory);
		Assert.assertFalse(targetDirectory.exists());
		Assert.assertTrue(sourceSubDirectory.listFiles().length == 0); //source sub-dir must be empty
		Assert.assertTrue(sourceDirectory.listFiles().length == 1); //source dir must be empty (1 == only sub-dir)
	}
	
	/* ************************** ConnectionUtil ************************** */
	
	@Test
	public void shouldRegisterOneDatabaseConnection() {
		Connection con = mockery.mock(Connection.class);
		DeveloperUtil.ConnectionUtil.addDatabaseConnection("ORCL_LOCAL", con);
		Assert.assertSame(con, DeveloperUtil.ConnectionUtil.getDatabaseConnection("ORCL_LOCAL"));
	}
	
	@Test
	public void shouldUpdateOneDatabaseConnection() {
		Connection con1 = mockery.mock(Connection.class, "con1");
		Connection con2 = mockery.mock(Connection.class, "con2");
		DeveloperUtil.ConnectionUtil.addDatabaseConnection("ORCL_LOCAL", con1);
		Assert.assertSame(con1, DeveloperUtil.ConnectionUtil.getDatabaseConnection("ORCL_LOCAL"));
		DeveloperUtil.ConnectionUtil.addDatabaseConnection("ORCL_LOCAL", con2);
		Assert.assertSame(con2, DeveloperUtil.ConnectionUtil.getDatabaseConnection("ORCL_LOCAL"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void shouldRiseExceptionWhenTryToRecuperateInexistentConnection() {
		DeveloperUtil.ConnectionUtil.getDatabaseConnection("FOO");
	}
	
	private void addFilesToDirectory(int qtd, File directory) {
		try {
			Random rand = new Random();
			for (int i = 0; i < qtd; i++) {
				File newFile = new File(directory, rand.nextInt(5000)+".txt");
				newFile.createNewFile();
			}
		} catch (IOException e) {}
	}

	private void add10FilesToDirectory(File directory) {
		try {
			for (int i = 0; i < 10; i++) {
				File newFile = new File(directory, "arq"+i+".txt");
				newFile.createNewFile();
			}
		} catch (IOException e) {}
	}

	private int whenReadFileShouldReturnFiveOcurrences(File fileToBeSearched) {
		return DeveloperUtil.FileUtil.fileLineStartPatternOccurrences("teste", fileToBeSearched);
	}

	private File givenAFileToSeachLines() {
		File fileToBeSearched = new File("src/test/resources", "line_start_pattern.txt");
		Assert.assertTrue(fileToBeSearched.exists()); // assert that it exists
		return fileToBeSearched;
	}

	private File recuperateMovedFile() {
		File fileMoved = new File("src/test/resources/dir", "tests.txt");
		return fileMoved;
	}
	
	private File recuperateCopiedFile() {
		File fileCopied = new File("src/test/resources/dir", "tests.txt");
		return fileCopied;
	}
	
	private File recuperateCopiedFileWithOtherName() {
		File fileCopied = new File("src/test/resources/dir", "same_file_other_name.txt");
		return fileCopied;
	}

	private void whenCopyTheFileToAnotherDirectory(File fileToBeCopied) {
		DeveloperUtil.FileUtil.copyFile(fileToBeCopied, 
				new File("src/test/resources/dir")); //same name
	}
	
	private void whenMoveTheFileToAnotherDirectory(File fileToBeMoved) {
		DeveloperUtil.FileUtil.moveFile(fileToBeMoved, 
				new File("src/test/resources/dir")); //same name
	}

	private void itCannotExistAnymoreAtSourceDirectory(File fileMoved) {
		Assert.assertFalse(fileMoved.exists()); // assert that it does not exists, it had to be moved now
	}
	
	private void itMustContinueToExistAtSourceDirectory(File fileCopied) {
		Assert.assertTrue(fileCopied.exists());
	}

	private File recuperateMovedFileWithOtherName() {
		File fileMoved = new File("src/test/resources/dir", "same_file_other_name.txt");
		return fileMoved;
	}

	private void whenMoveTheFileToAnotherDirectoryWithOtherName(File fileToBeMoved) {
		DeveloperUtil.FileUtil.moveFile(fileToBeMoved, 
								new File("src/test/resources/dir"),
								new File("same_file_other_name.txt")); //other name
	}
	
	private void whenCopyTheFileToAnotherDirectoryWithOtherName(File fileToBeCopied) {
		DeveloperUtil.FileUtil.copyFile(fileToBeCopied, 
								new File("src/test/resources/dir"),
								new File("same_file_other_name.txt")); //other name
	}

	private File givenAFile() {
		File fileToBeMoved = new File("src/test/resources", "tests.txt");
		Assert.assertTrue(fileToBeMoved.exists()); // assert that it exists
		return fileToBeMoved;
	}
}

class SetUp {

	static public void createTestFile() {
		File testsFile = new File("src/test/resources", "tests.txt");
		if (!testsFile.exists()) {
			try {
				testsFile.createNewFile();
				PrintWriter writer = new PrintWriter(testsFile);
				for (int i = 0; i < 1000; i++) {
					writer.write(randomString());
				}
				writer.flush(); writer.close();
			} catch (IOException e) {
			}
		}
	}

	public static String randomString() {
		Random rand = new Random();
		String string = "";
		for (int i = 0 ; i < rand.nextInt(5000); i++) {
			char character = randomChar(rand);
			string += character;
		}
		return string;
	}

	private static char randomChar(Random rand) {
		int ch = 51;
		do {
			ch = rand.nextInt(126);
		} while(ch < 32);
		return (char) ch;
	}

	static public void removeDirs() {
		File dir = new File("src/test/resources/dir");
		if (dir.exists())
			DeveloperUtil.FileUtil.deleteDirectory(dir);
	}
	
	static public void removeTestsFile() {
		File fileTests = new File("src/test/resources", "tests.txt");
		if (fileTests.exists())
			DeveloperUtil.FileUtil.deleteDirectory(fileTests);
	}
}