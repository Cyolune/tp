package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MailCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new MailCommand object
 */
public class MailCommandParser implements Parser<MailCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the MailCommand
     * and returns a MailCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public MailCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new MailCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, MailCommand.MESSAGE_USAGE), pe);
        }
    }
}
