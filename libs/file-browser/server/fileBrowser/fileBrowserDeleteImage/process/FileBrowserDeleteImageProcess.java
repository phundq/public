package source.page.fileBrowser.fileBrowserDeleteImage.process;

import java.util.ArrayList;
import java.util.List;

import source.common.ILogSender;
import source.common.database.DBAccessor;
import source.common.dto.ErrorDto;
import source.common.dto.request.AbstractRequest;
import source.common.dto.response.AbstractResponse;
import source.common.exception.DBException;
import source.common.exception.FatalException;
import source.common.exception.ProcessCheckErrorException;
import source.common.process.AbstractProcess;
import source.common.utility.ValidateUtility;
import source.page.fileBrowser.fileBrowserDeleteImage.dto.FileBrowserDeleteImageRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserGetFile.process.FileBrowserGetFileProcess;
import source.page.image.dto.ImageConfigDto;
import source.page.imageDelete.dto.ImageDeleteRequest;
import source.page.imageDelete.dto.ImageDeleteResponse;
import source.page.imageDelete.process.ImageDeleteProcess;
import source.page.imageSave.dto.ImageSaveDto;

/**
 * Screen: File Browser Delete File
 * Summary: display files (image) in current folder after delete
 */
public class FileBrowserDeleteImageProcess extends AbstractProcess {

	public FileBrowserDeleteImageProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		FileBrowserDeleteImageRequest req = (FileBrowserDeleteImageRequest) request;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;

		// validation
		if (req.fileBrowserDeleteImageDto.currentFolder == null) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "Please provide folder path!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
		if (req.folderConfigRequest == null || ValidateUtility.IsNull(req.folderConfigRequest.userRoleFv)) {
			ErrorDto errorDto = new ErrorDto();
			errorDto.controlID = "admin";
			errorDto.errMsg = "user config not found!";
			List<ErrorDto> errorDtos = new ArrayList<ErrorDto>();
			errorDtos.add(errorDto);
			res.setFatalError(errorDtos);
			return res;
		}
		
//		if (FolderConfigUseRole.AMIN.equals(req.folderConfigRequest.userRoleFv)) {
//			insertImageRoleAdmin(dba, request, response, parentResponse);
//			setFileResponse(dba, req, res, parentResponse);
//			return res;
//		}
		
		insertImageRoleOther(dba, request, response, parentResponse);
		setFileResponse(dba, req, res, parentResponse);
		return res;
	}
	
	private void insertImageRoleOther(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) {
		FileBrowserDeleteImageRequest req = (FileBrowserDeleteImageRequest) request;
		ArrayList<ImageSaveDto> imageSaveDtos = (ArrayList<ImageSaveDto>) req.fileBrowserDeleteImageDto.imageDeleteDtos;
		ImageConfigDto imageConfigDto = (ImageConfigDto) req.imageConfigDto;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;
		try {
			for (ImageSaveDto reqRow : imageSaveDtos) {
				// Request
				ImageDeleteRequest imageDeleteRequest = new ImageDeleteRequest();
				imageDeleteRequest.access = req.access;
				imageDeleteRequest.imageConfigDto = imageConfigDto;
				imageDeleteRequest.imageDeleteDto.imagePathFv = reqRow.path;

				// Process
				ImageDeleteProcess imageDeleteProcess = new ImageDeleteProcess(this);

				// Response
				ImageDeleteResponse imageDeleteResponse = new ImageDeleteResponse();

				imageDeleteResponse = (ImageDeleteResponse) imageDeleteProcess.process(dba, imageDeleteRequest,
						imageDeleteResponse, parentResponse);
				res.addFatalErrorList(imageDeleteResponse.getFatalError());
				res.addNormalErrorList(imageDeleteResponse.getNormalError());
			}

		} catch (FatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessCheckErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void setFileResponse(DBAccessor dba, FileBrowserDeleteImageRequest req, FileBrowserGetFileResponse res, AbstractResponse parentResponse) {
		try {
			FileBrowserGetFileRequest fileBrowserGetFileRequest = new FileBrowserGetFileRequest();
			fileBrowserGetFileRequest.access = req.access;
			fileBrowserGetFileRequest.folderConfigRequest = req.folderConfigRequest;
			fileBrowserGetFileRequest.fileBrowserGetFileDto.currentFolder = req.fileBrowserDeleteImageDto.currentFolder;
			
			FileBrowserGetFileProcess fileBrowserGetFileProcess = new FileBrowserGetFileProcess(this);
			
			res = (FileBrowserGetFileResponse) fileBrowserGetFileProcess.process(dba, fileBrowserGetFileRequest, res, parentResponse);
		} catch (FatalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProcessCheckErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	@Override
	protected AbstractResponse createNewResponse(AbstractRequest request) {
		return new FileBrowserGetFileResponse();
	}
}
