package it.krzeminski.gitinfo.types

data class CommitMetadata(
        val sha1: String,
        val timeUnixTimestamp: Long)

data class GitInfo(
        val latestCommit: CommitMetadata,
        val containsUncommittedChanges: Boolean)
