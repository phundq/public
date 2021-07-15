package source.page.fileBrowser.fileBrowserGetFolder.webservice;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import source.common.dto.response.AbstractResponse;
import source.common.process.AbstractProcess;
import source.common.webservice.AbstractWebService;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderRequest;
import source.page.fileBrowser.fileBrowserGetFolder.dto.FileBrowserGetFolderResponse;
import source.page.fileBrowser.fileBrowserGetFolder.process.FileBrowserGetFolderProcess;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
@Path("/")
public class FileBrowserGetFolderService extends AbstractWebService {

	@POST
	@Path("/FileBrowserGetFolder")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public FileBrowserGetFolderResponse product(FileBrowserGetFolderRequest request) {
		AbstractResponse abs = super.executeProcess(request);
		return (FileBrowserGetFolderResponse) abs;
	}

	@Override
	protected AbstractProcess getProcess() {
		return new FileBrowserGetFolderProcess(this);
	}
}

