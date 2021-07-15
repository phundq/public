package source.page.fileBrowser.fileBrowserAddImage.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.fileBrowser.fileBrowserAddImage.dto.FileBrowserAddImageRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserAddImage.process.FileBrowserAddImageProcess;

/**
 * Screen: File Browser Add File
 * Summary: display files (image) in current folder after add new
 */
@Path("/")
public class FileBrowserAddImageService extends AbstractWebService {

	@POST
	@Path("/FileBrowserAddImage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileBrowserGetFileResponse product(FileBrowserAddImageRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (FileBrowserGetFileResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new FileBrowserAddImageProcess(this);
	}
}

