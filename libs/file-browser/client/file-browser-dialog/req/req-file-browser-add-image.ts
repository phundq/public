import { ImageConfigDto, ReqImageSaveDto } from './../../../common/image';
import { ReqFolderConfig } from './req-folder-config';
import { Req } from '../../../common/request';
export interface ReqFileBrowserAddImageDto {
    currentFolder: string;
    imageSaveDtos: ReqImageSaveDto[];
}

export interface ReqFileBrowserAddImage extends Req {
    fileBrowserAddImageDto: ReqFileBrowserAddImageDto;
    folderConfigRequest: ReqFolderConfig;
    imageConfigDto: ImageConfigDto;
}