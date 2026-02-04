package si.um.feri.measurements;

/**
 * Demo-only class to trigger Sonar's hardcoded-credential rule.
 * Not used by application code.
 */
public final class DemoSonarSecurityTrigger {

    private DemoSonarSecurityTrigger() {
    }

    // Intentional demo string for Sonar detection (hardcoded credential rule).
    public static final String DEMO_ADMIN_PASSWORD = "demo-admin-123";
}
