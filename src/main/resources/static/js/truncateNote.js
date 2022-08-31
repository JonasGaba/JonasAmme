function truncateNote(reviewObject){

    var id = reviewObject.id;

    var note = reviewObject.note;
        if (note && note != 'null' && note.length>100){
            var noteId = 'note-' + id;
            var noteElement = document.getElementById(noteId);
            var noteTruncated = note.substring(0,98) + "...";
            noteElement.textContent = noteTruncated;
        }

}