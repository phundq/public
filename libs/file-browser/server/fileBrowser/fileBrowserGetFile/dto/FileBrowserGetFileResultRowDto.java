package source.page.fileBrowser.fileBrowserGetFile.dto;

import source.common.dto.AbstractDto;

/**
 * Screen: File Browser Get File
 * Summary: display files (image) in current folder
 */
public class FileBrowserGetFileResultRowDto extends AbstractDto {
	public String name;
	public String path;
	public String data;
	public long size;
	public String type;
	public String date;
}
