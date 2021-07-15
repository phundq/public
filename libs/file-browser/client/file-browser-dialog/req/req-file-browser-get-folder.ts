import { ReqFolderConfig } from './req-folder-config';
import { Req } from './../../../common/request';
export interface ReqFileBrowserGetFolderDto {
    currentFolder: string;
}

export interface ReqFileBrowserGetFolder extends Req {
    fileBrowserGetFolderDto: ReqFileBrowserGetFolderDto;
    folderConfigRequest: ReqFolderConfig
}