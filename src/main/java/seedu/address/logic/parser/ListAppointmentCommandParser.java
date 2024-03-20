package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_ID;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.logic.commands.ListAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentContainsPatientIdPredicate;
import seedu.address.model.appointment.AppointmentContainsPatientNamePredicate;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;

/**
 * Parses input arguments and creates a new ListCommand object
 */
public class ListAppointmentCommandParser implements Parser<ListAppointmentCommand> {
    @Override
    public ListAppointmentCommand parse(String args, List<Person> patients) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_NAME, CliSyntax.PREFIX_STUDENT_ID);

        List<Predicate<Appointment>> predicates = new ArrayList<>();

        // Checks if there is at least one prefix available.
        boolean hasAtLeastOnePrefixPresent = ParserUtil.hasAtLeastOnePrefixPresent(argMultimap, CliSyntax.PREFIX_NAME,
                CliSyntax.PREFIX_STUDENT_ID);

        if (!hasAtLeastOnePrefixPresent || !argMultimap.getPreamble().isEmpty()) {
            // If there is no prefix specified, then display all records.
            // TODO: Show an error message here.
            return new ListAppointmentCommand(PREDICATE_SHOW_ALL_APPOINTMENTS);
        }


        // All these criterias are OR not AND
        if (argMultimap.getValue(CliSyntax.PREFIX_NAME).isPresent()) {
            Name name = ParserUtil.parseName(argMultimap.getValue(CliSyntax.PREFIX_NAME).get());
            predicates.add(new AppointmentContainsPatientNamePredicate(
                    Collections.singletonList(name.fullName),
                    patients
            ));
            //TODO: Implement query appointment by name
            System.out.println("Hello boss");
        }

        if (argMultimap.getValue(CliSyntax.PREFIX_STUDENT_ID).isPresent()) {
            int studentId = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_STUDENT_ID).get()).getOneBased();
            predicates.add(
                    new AppointmentContainsPatientIdPredicate(Collections.singletonList(String.valueOf(studentId)))
            );
        }
        // Combine predicates with AND logic
        Predicate<Appointment> combinedPredicate = predicates.stream()
                .reduce(p -> true, Predicate::and);

        return new ListAppointmentCommand(combinedPredicate);
    }

    @Override
    public ListAppointmentCommand parse(String userInput) throws ParseException {
        return null;
    }

}
