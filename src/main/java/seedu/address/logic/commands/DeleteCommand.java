package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.patient.Patient;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "deletep";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the patient identified by the index number used in the displayed patients list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Patient: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        assert model != null : "Model should not be null.";

        List<Patient> lastShownList = model.getFilteredPersonList();
        List< Appointment> lastShownAppointmentList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= Patient.getIdTracker()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        // We try to find the person based on the given studentId.
        Optional<Patient> personToDelete = lastShownList.stream()
                .filter(person -> person.getSid() == targetIndex.getOneBased())
                .findFirst();

        List<Appointment> appointmentToDelete = lastShownAppointmentList.stream()
                .filter(appointment -> appointment.getPatientId().patientId == targetIndex.getOneBased())
                .collect(Collectors.toList());

        if (personToDelete.isPresent()) {
            // Delete the person if it was successfully found.
            model.deletePerson(personToDelete.get());
            for (Appointment appointment : appointmentToDelete) {
                model.deleteAppointment(appointment);
            }
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS,
                    Messages.format(personToDelete.get())));

        } else {
            // Otherwise we just throw an error.
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
