package ru.skillbranch.devintensive.models

class Bender(
        var status: Status = Status.NORMAL,
        var question: Question = Question.NAME,
        var wrongAnswer: Int = 0
) {

    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> = when (question) {
        Question.IDLE -> question.question to status.color
        else -> "${validateAnswer(answer)}\n${question.question}" to status.color
    }

    private fun validateAnswer(answer: String): String =
            if (question.validate(answer) && question.answer.contains(answer.toLowerCase())) {
                question = question.nextQuestion()
                "Отлично - ты справился"
            } else {
                if (status == Status.CRITICAL) {
                    resetStatus()
                    "Это неправильный ответ. Давай все по новой"
                } else {
                    status = status.nextStatus()
                    "Это неправильный ответ"
                }
            }

    private fun resetStatus() {
        status = Status.NORMAL
        question = Question.NAME
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status = values()[(ordinal + 1) % values().size]
    }

    enum class Question(
            val question: String,
            val answer: List<String>
    ) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion() = PROFESSION
            override fun validate(answer: String): Boolean = answer.trim().firstOrNull()?.isUpperCase()
                    ?: false
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion() = MATERIAL
            override fun validate(answer: String): Boolean = answer.trim().firstOrNull()?.isLowerCase()
                    ?: false
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion() = BDAY
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("\\d")).not()
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion() = SERIAL
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("^[0-9]*$"))
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion() = IDLE
            override fun validate(answer: String): Boolean = answer.trim().contains(Regex("^[0-9]{7}$"))
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion() = IDLE
            override fun validate(answer: String): Boolean = true
        };

        abstract fun nextQuestion(): Question
        abstract fun validate(answer: String): Boolean
    }

}