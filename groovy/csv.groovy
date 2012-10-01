new File(args[0]).splitEachLine(",") {fields ->
    println "Field 0: ${fields[0]}, Field 1: ${fields[1]}, Field 2: ${fields[2]}"
}
