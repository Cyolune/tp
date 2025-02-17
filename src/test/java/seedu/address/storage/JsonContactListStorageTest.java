package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalContactList;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ContactList;
import seedu.address.model.ReadOnlyContactList;

public class JsonContactListStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonContactListStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readContactList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readContactList(null));
    }

    private java.util.Optional<ReadOnlyContactList> readContactList(String filePath) throws Exception {
        return new JsonContactListStorage(Paths.get(filePath)).readContactList(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readContactList("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readContactList("notJsonFormatContactList.json"));
    }

    @Test
    public void readContactList_invalidPersonContactList_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readContactList("invalidPersonContactList.json"));
    }

    @Test
    public void readContactList_invalidAndValidPersonContactList_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readContactList("invalidAndValidPersonContactList.json"));
    }

    @Test
    public void readAndSaveContactList_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempContactList.json");
        ContactList original = getTypicalContactList();
        JsonContactListStorage jsonContactListStorage = new JsonContactListStorage(filePath);

        // Save in new file and read back
        jsonContactListStorage.saveContactList(original, filePath);
        ReadOnlyContactList readBack = jsonContactListStorage.readContactList(filePath).get();
        assertEquals(original, new ContactList(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonContactListStorage.saveContactList(original, filePath);
        readBack = jsonContactListStorage.readContactList(filePath).get();
        assertEquals(original, new ContactList(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonContactListStorage.saveContactList(original); // file path not specified
        readBack = jsonContactListStorage.readContactList().get(); // file path not specified
        assertEquals(original, new ContactList(readBack));

    }

    @Test
    public void saveContactList_nullContactList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContactList(null, "SomeFile.json"));
    }

    /**
     * Saves {@code contactList} at the specified {@code filePath}.
     */
    private void saveContactList(ReadOnlyContactList contactList, String filePath) {
        try {
            new JsonContactListStorage(Paths.get(filePath))
                    .saveContactList(contactList, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveContactList_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveContactList(new ContactList(), null));
    }
}
