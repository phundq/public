package source.page.fileBrowser.fileBrowserGetFile.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserGetFile.process.FileBrowserGetFileProcess;

/**
 * Screen: File Browser Get File
 * Summary: display files (image) in current folder
 */
@Path("/")
public class FileBrowserGetFileService extends AbstractWebService {

	@POST
	@Path("/FileBrowserGetFile")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileBrowserGetFileResponse product(FileBrowserGetFileRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (FileBrowserGetFileResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new FileBrowserGetFileProcess(this);
	}
}

