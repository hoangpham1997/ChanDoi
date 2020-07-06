package vn.softdreams.ebweb.service.util;

public final class DeployConstants {

    // builMode = 1: dev CRM
    // builMode = 2: test CRM
    // builMode = 3: that CRM
    private static final int buildMode = 3;

    private DeployConstants() {
    }

    public static String getCrmUrl()
    {
        if (buildMode == 1) {
            return "http://localhost:81/api/updateStatusPackage";
        } else if (buildMode == 2) {
            return "http://14.225.3.218:81/api/updateStatusPackage";
        } else if (buildMode == 3) {
            return "http://crm.softdreams.vn/api/updateStatusPackage";
        }
        return "";
    }

}
