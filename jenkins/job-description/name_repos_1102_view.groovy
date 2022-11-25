 
listView('name_repos_1102 Jobs') {
    description('name_repos_1102 Jobs')
    jobs {
        regex('name_repos_1102_.+')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
