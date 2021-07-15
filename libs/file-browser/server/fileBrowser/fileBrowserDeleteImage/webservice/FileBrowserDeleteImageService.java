package source.page.fileBrowser.fileBrowserDeleteImage.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.fileBrowser.fileBrowserDeleteImage.dto.FileBrowserDeleteImageRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserDeleteImage.process.FileBrowserDeleteImageProcess;

/**
 * Screen: File Browser Delete File
 * Summary: display files (image) in current folder after delete
 */
@Path("/")
public class FileBrowserDeleteImageService extends AbstractWebService {

	@POST
	@Path("/FileBrowserDeleteImage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileBrowserGetFileResponse product(FileBrowserDeleteImageRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (FileBrowserGetFileResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new FileBrowserDeleteImageProcess(this);
	}
}

