package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentList;
import seedu.address.model.appointment.ReadOnlyAppointmentList;
import seedu.address.model.patient.Patient;
import seedu.address.model.patientfeedbackreport.PatientFeedbackReport;
import seedu.address.model.patientfeedbackreport.PatientFeedbackReportList;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final PatientList patientList;

    private final AppointmentList appointmentList;
    private final UserPrefs userPrefs;
    private final PatientFeedbackReportList reportFeedbackList;
    private final FilteredList<Patient> filteredPatients;
    private final FilteredList<Appointment> filteredAppointments;

    /**
     * Initializes a ModelManager with the given patientList and userPrefs.
     */
    public ModelManager(ReadOnlyPatientList patientList, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(patientList, userPrefs);

        logger.fine("Initializing with patientList book: " + patientList + " and user prefs " + userPrefs);

        this.patientList = new PatientList(patientList);
        this.appointmentList = new AppointmentList();
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPatients = new FilteredList<>(this.patientList.getPersonList());
        filteredAppointments = new FilteredList<>(this.appointmentList.getAppointmentList());

        this.reportFeedbackList = new PatientFeedbackReportList(this.filteredPatients, this.filteredAppointments);
    }

    /**
     * Initializes a ModelManager with the given patientList, appointmentList and userPrefs.
     */
    public ModelManager(ReadOnlyPatientList patientList,
                        ReadOnlyAppointmentList appointmentList,
                        ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(patientList, appointmentList, userPrefs);

        logger.fine("Initializing with patientList book: " + patientList
                + ", appointment list: " + appointmentList + " and user prefs " + userPrefs);

        this.patientList = new PatientList(patientList);
        this.appointmentList = new AppointmentList(appointmentList);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPatients = new FilteredList<>(this.patientList.getPersonList());
        filteredAppointments = new FilteredList<>(this.appointmentList.getAppointmentList());

        this.reportFeedbackList = new PatientFeedbackReportList(this.filteredPatients, this.filteredAppointments);
    }

    public ModelManager() {
        this(new PatientList(), new AppointmentList(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getPatientListFilePath() {
        return userPrefs.getPatientListFilePath();
    }

    @Override
    public void setPatientListFilePath(Path patientListFilePath) {
        requireNonNull(patientListFilePath);
        userPrefs.setPatientListFilePath(patientListFilePath);
    }

    //=========== PatientList ================================================================================

    @Override
    public void setPatientList(ReadOnlyPatientList patientList) {
        this.patientList.resetData(patientList);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    @Override
    public void setAppointmentList(ReadOnlyAppointmentList appointmentList) {
        this.appointmentList.resetData(appointmentList);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    @Override
    public ReadOnlyPatientList getPatientList() {
        return patientList;
    }

    @Override
    public boolean hasPerson(Patient patient) {
        requireNonNull(patient);
        return patientList.hasPerson(patient);
    }

    @Override
    public void deletePerson(Patient target) {
        patientList.removePerson(target);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    @Override
    public void addPerson(Patient patient) {
        patientList.addPerson(patient);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    @Override
    public void setPerson(Patient target, Patient editedPatient) {
        requireAllNonNull(target, editedPatient);

        patientList.setPerson(target, editedPatient);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Patient> getFilteredPersonList() {
        return filteredPatients;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Patient> predicate) {
        requireNonNull(predicate);
        filteredPatients.setPredicate(predicate);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    /**
     * Get appointments from inside appointment list
     */
    @Override
    public ReadOnlyAppointmentList getAppointmentList() {
        return appointmentList;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return patientList.equals(otherModelManager.patientList)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPatients.equals(otherModelManager.filteredPatients);
    }

    //=========== AppointmentBook ================================================================================

    @Override
    public boolean hasAppointment(Appointment appointment) {
        requireNonNull(appointment);
        return appointmentList.hasAppointment(appointment);
    }

    @Override
    public void addAppointment(Appointment appointment) {
        appointmentList.addAppointment(appointment);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);

    }

    @Override
    public void setAppointment(Appointment target, Appointment editededitedAppointment) {
        requireAllNonNull(target, editededitedAppointment);

        appointmentList.setAppointment(target, editededitedAppointment);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    @Override
    public void deleteAppointment(Appointment target) {
        appointmentList.removeAppointment(target);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    //=========== Filtered Appointment List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return filteredAppointments;
    }

    @Override
    public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
        requireNonNull(predicate);
        filteredAppointments.setPredicate(predicate);
        this.reportFeedbackList.generateReportList(this.filteredPatients, this.filteredAppointments);
    }

    //=========== Reports =============================================================

    @Override
    public ObservableList<PatientFeedbackReport> getPatientFeedbackReportList() {
        return this.reportFeedbackList.getList();
    }
}
