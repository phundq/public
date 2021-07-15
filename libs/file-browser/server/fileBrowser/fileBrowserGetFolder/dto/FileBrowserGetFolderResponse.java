package source.page.fileBrowser.fileBrowserGetFolder.dto;

import java.util.ArrayList;

import source.common.dto.response.AbstractResponse;
import source.page.fileBrowser.dto.CurrentFolder;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
public class FileBrowserGetFolderResponse extends AbstractResponse {
	public CurrentFolder currentFolder = new CurrentFolder();
	public ArrayList<FileBrowserGetFolderResultRowDto> folders = new ArrayList<FileBrowserGetFolderResultRowDto>();
}
