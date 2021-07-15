package source.page.fileBrowser.fileBrowserGetFile.dto;

import java.util.ArrayList;

import source.common.dto.response.AbstractResponse;
import source.page.fileBrowser.dto.CurrentFolder;

/**
 * Screen: File Browser Get File
 * Summary: display files (image) in current folder
 */
public class FileBrowserGetFileResponse extends AbstractResponse {
	public CurrentFolder currentFolder = new CurrentFolder();
	public ArrayList<FileBrowserGetFileResultRowDto> files = new ArrayList<FileBrowserGetFileResultRowDto>();
}
