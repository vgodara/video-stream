rootProject.name = "video-stream"

val INCLUDE_ANDROID: String by extra
include("common")
include("common:utilities")
include("common:response")
include("backend")
include("backend:common")
include("backend:users")
include("backend:users:common")
include("backend:users:entity")
include("backend:users:usecase")
include("backend:users:controller")
include("backend:users:datasource")
include("backend:users:database")
include("backend:users:application")
