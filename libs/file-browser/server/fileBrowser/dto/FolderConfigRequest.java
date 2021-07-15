package source.page.fileBrowser.dto;

public class FolderConfigRequest {
	public String userRoleFv;
	public String storeCdFv;
	public String specTarget;

	public class FolderConfigUseRole {
		public static final String CUSTOMER = "CUSTOMER";
		public static final String AMIN = "ADMIN";
	}
	
	public class FileBrowserSpecTarget {
		public static final String PRODUCT_GROUP_CERTIFICATE = "PRODUCT_GROUP_CERTIFICATE";
	}
}
