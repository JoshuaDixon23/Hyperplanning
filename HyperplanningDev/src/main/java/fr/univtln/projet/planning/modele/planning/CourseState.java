package fr.univtln.projet.planning.modele.planning;

public enum CourseState {
    SCHEDULED, // scheduled, but incompleted course (no professor and/or no room)
    //WITHOUT_ROOM,
    //WITHOUT_PROF,
    COMPLETED, // all the information needed is given
    //IN_PROGRESS, // we would not add temporal aspect here
    //FINISHED,
    REPORTED,
    ANNULATED,
    MODIFICATION_REQUEST
}
