package source.page.image.dto;

/**
 * Summary: Config image
 */
public class ImageConfigDto {
	public String targetTypeSave = ImageTargetTypeSave.PRODUCT;
	public String targetCd;
	public String extension = ImageExtension.PNG;
	public String suffixPath = "";

	public class ImageTargetTypeSave {
		public static final String PRODUCT = "product"; // Product image
		public static final String PRODUCT_GROUP = "product-group";
		public static final String PRODUCER = "producer";
		public static final String UPLOAD = "upload";
		public static final String DIARY = "diary";
	}
	
	public class ImageSuffixPath {
		public static final String BACKGROUND = "background"; // Product image
		public static final String INTRO = "intro";
		public static final String AVATAR = "avatar";
	}

	public class ImageExtension {
		public static final String JPG = ".jpg";
		public static final String PNG = ".png";
		public static final String JPEG = ".jpeg";
	}
}
