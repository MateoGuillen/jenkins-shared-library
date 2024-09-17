def static eliminarCaracteresGit(lista) {
    def LIST_AUX = []
    for (rama in lista.split("\n")) {
        def processedRama = rama.replaceAll('origin/', '')
        if (!LIST_AUX.contains(processedRama)) {
            LIST_AUX.push(processedRama)
        }
    }
    return LIST_AUX
}