package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.commons.util.DateUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.patient.Patient;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    public static final String MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX =
            "The appointment index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d persons listed!";
    public static final String MESSAGE_APPOINTMENTS_LISTED_OVERVIEW = "%1$d appointments listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_INVALID_PATIENT_ID = "No such patient with id %1$d exists";

    public static final String MESSAGE_INVALID_DATE = "Invalid date format. "
            + "Please use the format yyyy-MM-dd";
    public static final String MESSAGE_INVALID_DATE_TIME = "Invalid date time format. "
            + "Please use the format yyyy-MM-dd HH:mm";

    public static final String MESSAGE_DATETIME_IN_THE_PAST =
            "Date and time cannot be in the past";

    public static final String MESSAGE_INVALID_START_END_DATETIME = "Start datetime must be before end datetime";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Patient patient) {
        final StringBuilder builder = new StringBuilder();
        builder.append(patient.getName())
                .append("; Phone: ")
                .append(patient.getPhone())
                .append("; Email: ")
                .append(patient.getEmail())
                .append("; Associated with: ");
        patient.getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Formats the {@code appointment} for display to the user.
     */
    public static String formatAppointment(Appointment appointment) {
        final StringBuilder builder = new StringBuilder();
        builder.append(appointment.getAppointmentId())
                .append("; PatientId: ")
                .append(appointment.getPatientId())
                .append("; Start: ")
                .append(DateUtil.formatDateTime(appointment.getStartDateTime().getDateTimeValue()))
                .append("; End: ")
                .append(DateUtil.formatDateTime(appointment.getEndDateTime().getDateTimeValue()))
                .append("; Attend: ")
                .append(appointment.getAttendedStatus())
                .append("; Score: ")
                .append(appointment.getFeedbackScore())
                .append("; Description: ")
                .append((appointment.getAppointmentDescription()));
        return builder.toString();
    }
}
