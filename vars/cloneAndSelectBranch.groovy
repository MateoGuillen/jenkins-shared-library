def call(String nombreMicroservicio, String rama, String gitCredentials, String repositoryUrl, String ambiente, String tag) {
    dir("${nombreMicroservicio}") {
        git branch: "${rama}", credentialsId: "${gitCredentials}", url: "${repositoryUrl}"
        echo 'git clonado....'
        def branchList = sh(returnStdout: true, script: "git branch --sort=-committerdate --format='%(refname:short)' --all").trim()

        def branchListAux = GitUtils.eliminarCaracteresGit(branchList)

        def selectedBranch = input(
            message: "Versiona a desplegar en ${ambiente}",
            parameters: [
                [$class: 'ChoiceParameterDefinition', choices: branchListAux, name: 'rama', description: "Seleccione la rama a desplegar en ${ambiente}"]
            ]
        )

        sh script: "git checkout ${selectedBranch}"

        // Logica para TAG y imageTag
        def tagFound = false
        def imageTag
        if(tag != "") {
            echo ("Se encontró el TAG = "+ tag)
            echo ("Despliegue con TAG, se asigna versión:" + tag)
            tagFound = true
            imageTag  = "${nombreMicroservicio}:${tag}"
            echo ("El TAG GENERADO ES " + imageTag)
        } else {
            echo ("No se encontró TAG para este despliegue, se asigna numero de compilación de Jenkins: " + env.BUILD_NUMBER)
            tag = env.BUILD_NUMBER
            tagFound = false
            imageTag  = "${nombreMicroservicio}:${selectedBranch}"
        }

        return [selectedBranch: selectedBranch, tag: tag, tagFound: tagFound, imageTag: imageTag]
    }
}
