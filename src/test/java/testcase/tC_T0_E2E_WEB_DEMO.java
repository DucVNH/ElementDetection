package testcase;

import org.testng.asserts.SoftAssert;
import page.admidio.AdmidioLoginPage;
import page.admidio.DocumentsAndFilesPage;
import page.admidio.GroupsAndRolesPage;
import page.automationintesting.SeleniumTestPage;
import core.testng.BaseTest;
import org.testng.annotations.Test;
import page.mantisbt.ManagePage;
import page.mantisbt.MantisLoginPage;
import page.mantisbt.SummaryPage;
import page.mantisbt.ViewAllBugPage;
import page.orangehrm.OrangeHRMLoginPage;
import page.orangehrm.ViewEmployeeList;
import page.orangehrm.ViewSystemUsers;

public class tC_T0_E2E_WEB_DEMO extends BaseTest {
    SeleniumTestPage seleniumTestPage = new SeleniumTestPage(webDriver);

    page.mantisbt.SidePanel mantisSidePanel = new page.mantisbt.SidePanel(webDriver);
    MantisLoginPage mantisLoginPage = new MantisLoginPage(webDriver);
    ViewAllBugPage viewAllBugPage = new ViewAllBugPage(webDriver);
    SummaryPage summaryPage = new SummaryPage(webDriver);
    ManagePage managePage = new ManagePage(webDriver);

    page.orangehrm.SidePanel orangeSidePanel = new page.orangehrm.SidePanel(webDriver);
    OrangeHRMLoginPage orangeHRMLoginPage = new OrangeHRMLoginPage(webDriver);
    ViewSystemUsers viewSystemUsers = new ViewSystemUsers(webDriver);
    ViewEmployeeList viewEmployeeList = new ViewEmployeeList(webDriver);

    page.admidio.SidePanel admidioSidePanel = new page.admidio.SidePanel(webDriver);
    AdmidioLoginPage admidioLoginPage = new AdmidioLoginPage(webDriver);
    DocumentsAndFilesPage documentsAndFilesPage = new DocumentsAndFilesPage(webDriver);
    GroupsAndRolesPage groupsAndRolesPage = new GroupsAndRolesPage(webDriver);

    @Test
    public void tC_T0_E2E_WEB_DEMO_000() {
        SoftAssert softAssert = new SoftAssert();
        loadUrl("https://automationintesting.com/selenium/testpage/");
        seleniumTestPage.verifyPage(softAssert);
    }

    @Test
    public void tC_T0_E2E_WEB_DEMO_001() {
        String webVer = "2_24_0";
        SoftAssert softAssert = new SoftAssert();

        loadUrl(String.format("http://localhost/mantisbt_%s/my_view_page.php", webVer));
        mantisLoginPage.logIn("administrator", "root");
        mantisSidePanel.verifyPage(softAssert);
        mantisSidePanel.goToViewIssues();
        viewAllBugPage.verifyPage(softAssert);
        mantisSidePanel.goToSummary();
        summaryPage.verifyPage(softAssert);
        mantisSidePanel.goToManage();
        managePage.verifyPage(softAssert);
    }

    @Test
    public void tC_T0_E2E_WEB_DEMO_002() {
        String webVer = "5_0";
        SoftAssert softAssert = new SoftAssert();

        loadUrl(String.format("http://localhost/orangehrm_%s/", webVer));
        orangeHRMLoginPage.logIn("administrator", "Sept_3rd2024");
        orangeSidePanel.verifyPage(softAssert);
        orangeSidePanel.goToPIM();
        viewEmployeeList.verifyPage(softAssert);
        orangeSidePanel.goToAdmin();
        viewSystemUsers.verifyPage(softAssert);
    }

    @Test
    public void tC_T0_E2E_WEB_DEMO_003() {
        String webVer = "4_2_12";
        SoftAssert softAssert = new SoftAssert();

        loadUrl(String.format("http://localhost/admidio_%s/", webVer));
        admidioLoginPage.logIn("administrator", "Sept_3rd2024");
        admidioSidePanel.verifyPage(softAssert);
        admidioSidePanel.goToDocsAndFiles();
        documentsAndFilesPage.verifyPage(softAssert);
        admidioSidePanel.goToGroupsAndRoles();
        groupsAndRolesPage.verifyPage(softAssert);
    }
}
