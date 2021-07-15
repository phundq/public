package source.page.fileBrowser.fileBrowserGetFolder.dto;

import source.common.dto.AbstractDto;

/**
 * Screen: File Browser Get Folder
 * Summary: display current folder and nearest childs
 */
public class FileBrowserGetFolderResultRowDto extends AbstractDto {
	public String name;
	public String path;
	public boolean hasChild;
}
