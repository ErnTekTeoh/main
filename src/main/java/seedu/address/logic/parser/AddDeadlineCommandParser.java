package seedu.address.logic.parser;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AddDeadlineCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.entity.Deadline;

import seedu.address.model.entity.Event;
import seedu.address.model.entity.Module;
import seedu.address.model.entity.CalendarItemName;

import java.util.stream.Stream;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

public class AddDeadlineCommandParser implements Parser<AddDeadlineCommand> {


    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddDeadlineCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MODULE, PREFIX_EVENT, PREFIX_NAME, PREFIX_REPEAT);

        if (!arePrefixesPresent(argMultimap, PREFIX_MODULE, PREFIX_EVENT, PREFIX_NAME, PREFIX_REPEAT)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddDeadlineCommand.MESSAGE_USAGE));
        }

        CalendarItemName module = ParserUtil.parseName(argMultimap.getValue(PREFIX_MODULE).get());
        CalendarItemName name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        CalendarItemName eventName = ParserUtil.parseName(argMultimap.getValue(PREFIX_EVENT).get());
        boolean isRepeated = ParserUtil.parseRepeat(argMultimap.getValue(PREFIX_REPEAT).get());

        Deadline deadline = new Deadline(name);
        Event event = new Event(eventName);
        event.setParentModule(new Module(module.toString()));
        deadline.setParentEvent(event);

        return new AddDeadlineCommand(deadline, isRepeated);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
