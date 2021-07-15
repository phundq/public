package source.page.fileBrowser.fileBrowserAddImage.process;

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
import source.page.fileBrowser.dto.FolderConfigRequest.FolderConfigUseRole;
import source.page.fileBrowser.fileBrowserAddImage.dto.FileBrowserAddImageRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileRequest;
import source.page.fileBrowser.fileBrowserGetFile.dto.FileBrowserGetFileResponse;
import source.page.fileBrowser.fileBrowserGetFile.process.FileBrowserGetFileProcess;
import source.page.image.dto.ImageConfigDto;
import source.page.image.dto.ImageConfigDto.ImageTargetTypeSave;
import source.page.imageSave.dto.ImageSaveDto;
import source.page.imageSave.dto.ImageSaveRequest;
import source.page.imageSave.dto.ImageSaveResponse;
import source.page.imageSave.process.ImageSaveProcess;

/**
 * Screen: File Browser Add File
 * Summary: display files (image) in current folder after add new
 */
public class FileBrowserAddImageProcess extends AbstractProcess {

	public FileBrowserAddImageProcess(ILogSender logSender) {
		super(logSender);
	}

	@Override
	public AbstractResponse process(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) throws FatalException, DBException, ProcessCheckErrorException {
		FileBrowserAddImageRequest req = (FileBrowserAddImageRequest) request;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;

		// validation
		if (req.fileBrowserAddImageDto.currentFolder == null) {
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
		
		if (FolderConfigUseRole.AMIN.equals(req.folderConfigRequest.userRoleFv)) {
			insertImageRoleAdmin(dba, request, response, parentResponse);
			setFileResponse(dba, req, res, parentResponse);
			return res;
		}
		
		
		insertImageRoleOther(dba, request, response, parentResponse);
		setFileResponse(dba, req, res, parentResponse);
		return res;
	}
	
	private void insertImageRoleOther(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) {
		FileBrowserAddImageRequest req = (FileBrowserAddImageRequest) request;
		ArrayList<ImageSaveDto> imageSaveDtos = (ArrayList<ImageSaveDto>) req.fileBrowserAddImageDto.imageSaveDtos;
		ImageConfigDto imageConfigDto = (ImageConfigDto) req.imageConfigDto;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;
		try {
			// Request
			for (ImageSaveDto reqRow : imageSaveDtos) {
				ImageSaveRequest imageSaveRequest = new ImageSaveRequest();
				imageSaveRequest.access = req.access;
				imageSaveRequest.imageConfigDto = imageConfigDto;
				imageSaveRequest.imageSaveDto = reqRow;

				imageSaveRequest.imageConfigDto.targetCd = req.access.storeCd;
				imageSaveRequest.imageConfigDto.targetTypeSave = ImageTargetTypeSave.UPLOAD;
				imageSaveRequest.imageConfigDto.suffixPath = req.fileBrowserAddImageDto.currentFolder;
				// Process
				ImageSaveProcess imageSaveProcess = new ImageSaveProcess(this);

				// Response
				ImageSaveResponse imageSaveResponse = new ImageSaveResponse();

				imageSaveResponse = (ImageSaveResponse) imageSaveProcess.process(dba, imageSaveRequest,
						imageSaveResponse, parentResponse);
				res.addFatalErrorList(imageSaveResponse.getFatalError());
				res.addNormalErrorList(imageSaveResponse.getNormalError());
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
	
	private void insertImageRoleAdmin(DBAccessor dba, AbstractRequest request, AbstractResponse response,
			AbstractResponse parentResponse) {
		FileBrowserAddImageRequest req = (FileBrowserAddImageRequest) request;
		ArrayList<ImageSaveDto> imageSaveDtos = (ArrayList<ImageSaveDto>) req.fileBrowserAddImageDto.imageSaveDtos;
		ImageConfigDto imageConfigDto = (ImageConfigDto) req.imageConfigDto;
		FileBrowserGetFileResponse res = (FileBrowserGetFileResponse) response;
		try {
			// Request
			for (ImageSaveDto reqRow : imageSaveDtos) {
				ImageSaveRequest imageSaveRequest = new ImageSaveRequest();
				imageSaveRequest.access = req.access;
				imageSaveRequest.imageConfigDto = imageConfigDto;
				imageSaveRequest.imageSaveDto = reqRow;

				imageSaveRequest.imageConfigDto.targetCd = "";
				imageSaveRequest.imageConfigDto.targetTypeSave = "";
				imageSaveRequest.imageConfigDto.suffixPath = req.fileBrowserAddImageDto.currentFolder;
				// Process
				ImageSaveProcess imageSaveProcess = new ImageSaveProcess(this);

				// Response
				ImageSaveResponse imageSaveResponse = new ImageSaveResponse();

				imageSaveResponse = (ImageSaveResponse) imageSaveProcess.process(dba, imageSaveRequest,
						imageSaveResponse, parentResponse);
				res.addFatalErrorList(imageSaveResponse.getFatalError());
				res.addNormalErrorList(imageSaveResponse.getNormalError());
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
	
	private void setFileResponse(DBAccessor dba, FileBrowserAddImageRequest req, FileBrowserGetFileResponse res, AbstractResponse parentResponse) {
		try {
			FileBrowserGetFileRequest fileBrowserGetFileRequest = new FileBrowserGetFileRequest();
			fileBrowserGetFileRequest.access = req.access;
			fileBrowserGetFileRequest.folderConfigRequest = req.folderConfigRequest;
			fileBrowserGetFileRequest.fileBrowserGetFileDto.currentFolder = req.fileBrowserAddImageDto.currentFolder;
			
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
