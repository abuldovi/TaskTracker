package abuldovi.apps.bugtracker.security;

public enum ApplicationUserPermission {
    USER_READ("student:read"),
    USER_WRITE("student:write"),
    COURSE_READ("student:read"),
    COURSE_WRITE("student:write"),
    ;

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
