import { ReqFolderConfig } from './req-folder-config';
import { Req } from '../../../common/request';
export interface ReqFileBrowserCreateFolderDto {
    currentFolder: string;
    newFolderName: string;
}

export interface ReqFileBrowserCreateFolder extends Req {
    fileBrowserCreateFolderDto: ReqFileBrowserCreateFolderDto;
    folderConfigRequest: ReqFolderConfig
}