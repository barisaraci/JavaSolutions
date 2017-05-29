package hw_file_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Main {

	private final String FILE_EXTENSION = ".ncat";
	private final String ENCODING = "UTF-8";
	
	private final String COMMAND_NEW = "new";
	private final String COMMAND_APPEND = "append";
	private final String COMMAND_SHOW = "show";
	private final String COMMAND_RENAME = "rename";
	private final String COMMAND_DELETE = "delete";
	private final String COMMAND_LIST = "list";
	private final String COMMAND_HELP = "help";
	private final String COMMAND_EXIT = "exit";

	private final String INVALID_MENU_COMMAND = "Invalid menu control command";
	private final String INVALID_NOTE_COMMAND = "Invalid note taking command";
	private final String INVALID_ARGUMENT_NUMBER = "Invalid number of arguments";
	private final String INVALID_FILE = "File does not exist";
	private final String INVALID_FILE_TO_DELETE = "File to delete does not exist";
	private final String INVALID_NOTE_NUMBER_TO_LIST = "You have no taken notes yet.";
	private final String INVALID_NOTE_NAME_TO_RENAME = "Invalid note name for renaming. It contains ' '.";
	private final String INVALID_NOTE_TO_PROCESS = "File already exists";

	private final String HELP_DETAILS = "noteCat 1.0.0 - simple console notetaking program\n"
										+ "USAGE: noteCat <COMMAND> [DATA]\n"
										+ "OPTIONS:\n"
										+ "new    [note] Create new note\n"
										+ "append [note] Append to an existing note\n"
										+ "show   [note] Display existing note\n"
										+ "rename [note] Rename existing note\n"
										+ "delete [note] Delete existing note\n"
										+ "list   [all ] List all notes taken so far.\n"
										+ "help          Prints this help menu\n"
										+ "exit          Terminates NoteCat";

	public static void main(String[] args) {
		Main noteCat = new Main();
		noteCat.run();
	}

	private void run() {
		Scanner scanner = new Scanner(System.in);

		String noteBeingTaken = "";
		String noteName = "";
		boolean isNoteBeingTaken = false;

		while (true) {
			if (!isNoteBeingTaken) {
				System.out.print("> ");

				String command = scanner.nextLine();
				String[] commandArguments = command.split(" ");
				int numberOfCommands = commandArguments.length;

				if (numberOfCommands == 1) {
					if (commandArguments[0].equalsIgnoreCase(COMMAND_HELP)) {
						System.out.println(HELP_DETAILS);
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_EXIT)) {
						break;
					} else {
						System.out.println(INVALID_MENU_COMMAND);
					}
				} else {
					if (commandArguments[0].equalsIgnoreCase(COMMAND_SHOW) && numberOfCommands == 2) {
						System.out.println(getNote(commandArguments[1]));
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_NEW) && numberOfCommands == 2) {
						noteName = commandArguments[1];
						File note = new File(noteName + FILE_EXTENSION);
						
						if (!note.exists()) {
							System.out.println("Enter your note");
							isNoteBeingTaken = true;
						} else {
							System.out.println(INVALID_NOTE_TO_PROCESS);
						}
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_APPEND) && numberOfCommands == 2) {
						noteName = commandArguments[1];
						File note = new File(noteName + FILE_EXTENSION);

						if (!note.exists()) {
							System.out.println(INVALID_FILE);
						} else {
							System.out.println("Enter your note");
							isNoteBeingTaken = true;
						}
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_RENAME) && numberOfCommands == 2) {
						System.out.println("Enter the new note name");
						renameNote(commandArguments[1], scanner.nextLine());
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_DELETE) && numberOfCommands == 2) {
						deleteNote(commandArguments[1]);
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_LIST) && numberOfCommands == 2) {
						listNotes(commandArguments[1]);
					} else if (commandArguments[0].equalsIgnoreCase(COMMAND_SHOW)
							|| commandArguments[0].equalsIgnoreCase(COMMAND_NEW)
							|| commandArguments[0].equalsIgnoreCase(COMMAND_APPEND)
							|| commandArguments[0].equalsIgnoreCase(COMMAND_DELETE)
							|| commandArguments[0].equalsIgnoreCase(COMMAND_RENAME)
							|| commandArguments[0].equalsIgnoreCase(COMMAND_LIST)) {
						System.out.println(INVALID_ARGUMENT_NUMBER);
					} else {
						System.out.println(INVALID_NOTE_COMMAND);
					}
				}
			} else {
				noteBeingTaken += scanner.nextLine() + System.lineSeparator();

				if (noteBeingTaken.contains("#END")) {
					int positionOfEnd = noteBeingTaken.indexOf("#END");

					String note = noteBeingTaken.substring(0, positionOfEnd);
					createOrAppendNote(noteName, note);

					noteBeingTaken = "";
					noteName = "";
					isNoteBeingTaken = false;
				}
			}
		}

		scanner.close();
		System.exit(0);
	}

	private void createOrAppendNote(String noteName, String noteContent) {
		String modifiedNote = "";
		File file = new File(noteName + FILE_EXTENSION);

		if (file.exists()) {
			modifiedNote += getNote(noteName) + System.lineSeparator();
		}

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName(ENCODING));
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

			if (!file.exists()) {
				file.createNewFile();
			}

			modifiedNote += noteContent;

			bufferedWriter.write(modifiedNote);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteNote(String noteName) {
		File file = new File(noteName + FILE_EXTENSION);
		if (!file.delete()) {
			System.out.println(INVALID_FILE_TO_DELETE);
		}
	}

	private void renameNote(String oldName, String newName) {
		if (newName.contains(" ")) {
			System.out.println(INVALID_NOTE_NAME_TO_RENAME);
		} else {
			File oldNote = new File(oldName + FILE_EXTENSION);
			File newNote = new File(newName + FILE_EXTENSION);

			if (!oldNote.exists()) {
				System.out.println(INVALID_FILE);
			} else if (newNote.exists()) {
				System.out.println(INVALID_NOTE_TO_PROCESS);
			} else {
				oldNote.renameTo(newNote);
			}
		}

	}

	private void listNotes(String listArgument) {
		File directory = new File(".");
		File notes[] = directory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File direction, String name) {
				return name.endsWith(FILE_EXTENSION);
			}
		});

		if (notes.length == 0) {
			System.out.println(INVALID_NOTE_NUMBER_TO_LIST);
		} else {
			boolean isNoteFound = false;
			for (File note : notes) {
				if (listArgument.equalsIgnoreCase("all")) {
					System.out.println(note.getName());
					isNoteFound = true;
				} else if ((listArgument + ".ncat").equalsIgnoreCase(note.getName())) {
					System.out.println(note.getName());
					isNoteFound = true;
				}
			}
			if (!isNoteFound) {
				System.out.println(INVALID_FILE);
			}
		}
	}

	private String getNote(String noteName) {
		String note = "";

		try {
			FileInputStream fileInputStream = new FileInputStream(new File(noteName + FILE_EXTENSION));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName(ENCODING));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;

			while ((line = bufferedReader.readLine()) != null) {
				note += line + System.lineSeparator();
			}

			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.print(INVALID_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return note.trim();
	}

}
