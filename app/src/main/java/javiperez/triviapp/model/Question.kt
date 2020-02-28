package javiperez.triviapp.model

data class Question (
    val category : Category,
    val multiple : Boolean,
    val difficulty : String,
    val question : String,
    val answers : List<String>,
    val correct_answer : String
) {

    override fun toString() : String {
        return question + " " + answers + "->" + correct_answer
    }
}