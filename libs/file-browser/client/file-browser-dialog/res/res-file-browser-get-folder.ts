import { Res } from './../../../common/response';
export interface CurrentFolder {
    name: string;
    path: string;
}

export interface ResFileBrowserGetFolderDto {
    name: string;
    path: string;
    hasChild?: boolean;
    folders?: ResFileBrowserGetFolderDto[];
}

export interface ResFileBrowserGetFolder extends Res {
    currentFolder: CurrentFolder;
    folders: ResFileBrowserGetFolderDto[];
}
