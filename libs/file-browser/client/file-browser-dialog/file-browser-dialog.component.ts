import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { AppInitService } from 'src/app/app-initializer';
import { Image, ReqImageSaveDto } from 'src/app/common/image';
import { SnackBarService } from 'src/app/snack-bar/snack-bar-service';
import { CommonService } from './../../common/common-service';
import { ImageConfigDto } from './../../common/image';
import { DialogService } from './../dialog-service';
import { ImagePreviewDialogComponent } from './../image-preview-dialog/image-preview-dialog.component';
import { FileBrowserService } from './file-browser-dialog-service';
import { FileBrowserFolderEditData, FileBrowserFolderEditDialogComponent } from './file-browser-folder-edit-dialog/file-browser-folder-edit-dialog.component';
import { ContextMenuAction, FileBrowserSpecTarget } from './file-browser.i';
import { ReqFileBrowserAddImage } from './req/req-file-browser-add-image';
import { ReqFileBrowserCreateFolder } from './req/req-file-browser-create-folder';
import { ReqFileBrowserDeleteImage } from './req/req-file-browser-delete-image';
import { ReqFileBrowserGetFile } from './req/req-file-browser-get-file';
import { ReqFileBrowserGetFolder } from './req/req-file-browser-get-folder';
import { ReqFolderConfig } from './req/req-folder-config';
import { ResFileBrowserGetFileDto } from './res/res-file-browser-get-file';
import { ResFileBrowserGetFolderDto } from './res/res-file-browser-get-folder';
export interface FileBrowserData {
  specTarget: FileBrowserSpecTarget
};
@Component({
  selector: 'app-file-browser-dialog',
  templateUrl: './file-browser-dialog.component.html',
  styleUrls: ['./file-browser-dialog.component.scss'],
  // changeDetection: ChangeDetectionStrategy.OnPush
})
export class FileBrowserDialogComponent implements OnDestroy {
  //CONFIG =============================================================================================================================================
  public apiUrlReadImage = this.appInitializer.config.apiUrlReadImage;

  public simpleMenuData: ResFileBrowserGetFolderDto[] = [];

  public isShowSideBar: boolean = true;

  public folderContextmenu = false;
  public folderContextmenuX = 0;
  public folderContextmenuY = 0;

  public imageContextmenu = false;
  public imageContextmenuX = 0;
  public imageContextmenuY = 0;

  //DECLARE PARAM ======================================================================================================================================

  public subscribeSearch: Subscription;
  public subscriptionConfirm: Subscription;

  public fileList: ResFileBrowserGetFileDto[] = [];

  public folderActive: string = '';
  public folderSelected: ResFileBrowserGetFolderDto = {
    path: '',
    name: ''
  };

  public imageSelected: ResFileBrowserGetFileDto = {
    path: '',
    name: '',
    data: '',
    date: '',
    size: 0
  };

  public listImageUpload: Image[] = [];
  public imageErrorList: string[] = [];
  constructor(
    public dialogRef: MatDialogRef<FileBrowserDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: FileBrowserData,
    public commonService: CommonService,
    public dialogService: DialogService,
    public snackBarService: SnackBarService,
    public fileBrowserService: FileBrowserService,
    public http: HttpClient,
    private appInitializer: AppInitService

  ) {
    // Init request
    let reqFolderInit: ReqFileBrowserGetFolder = {
      fileBrowserGetFolderDto: {
        currentFolder: ""
      },
      folderConfigRequest: this.initFolderConfigDto()
    }

    // Call Api init folder and file browser

    this.fileBrowserService.getFolders(reqFolderInit)
      .subscribe(
        (data) => {
          this.simpleMenuData = data.folders;
          if (this.data && this.data.specTarget) {
            this.getFiles(null);
          }
        })
  }

  ngOnDestroy() {
    if (this.subscribeSearch) {
      this.subscribeSearch.unsubscribe();
      this.subscribeSearch = undefined;
    }
  }

  getFolders(currentFolder: string) {
    let req: ReqFileBrowserGetFolder = {
      fileBrowserGetFolderDto: {
        currentFolder: currentFolder,
      },
      folderConfigRequest: this.initFolderConfigDto()
    }

    return this.fileBrowserService.getFolders(req);
  }

  getFiles(currentFolder: string) {
    let req: ReqFileBrowserGetFile = {
      fileBrowserGetFileDto: {
        currentFolder: currentFolder
      },
      folderConfigRequest: this.initFolderConfigDto()
    }

    this.subscribeSearch = this.fileBrowserService.getFiles(req)
      .subscribe((data) => {
        this.fileList = data.files;
      })
  }

  handlerSelectFolder(data: ResFileBrowserGetFolderDto) {
    this.folderActive = data.path.replace(/[\\]/g, ' > ');
    this.folderSelected = JSON.parse(JSON.stringify(data));
    this.subscribeSearch = this.getFolders(data.path).subscribe(
      res => {
        this.clearProgressState();
        this.getFiles(data.path);
        if (res) data.folders = res.folders;
      }
    );
  }

  handlerSelectImage(data: ResFileBrowserGetFileDto) {
    this.dialogRef.close(data.path);
  }

  doClickAddFolderButton() {
    this.dialogService.open(FileBrowserFolderEditDialogComponent, {
      autoFocus: false
    }).subscribe(
      (data) => {
        if (data) {
          this.doAddFolder(data.name);
        }
      }
    )
  }

  doAddFolder(name: string) {
    if (!name && name.trim() === '')
      return;
    let req: ReqFileBrowserCreateFolder = {
      fileBrowserCreateFolderDto: {
        currentFolder: this.folderSelected.path,
        newFolderName: name
      },
      folderConfigRequest: this.initFolderConfigDto()
    }

    this.subscribeSearch = this.fileBrowserService.createFolder(req)
      .subscribe(
        data => {
          if (!this.commonService.hasError(data)) {
            this.folderSelected.folders = data.folders;
            this.folderSelected.hasChild = true;
            this.commonService.openNotificationDialog("Add folder", "Add folder success!")
          }
        }
      )
  }

  selectImage(e, item: ResFileBrowserGetFileDto, selectedItem: HTMLElement) {
    e.stopPropagation();
    this.disableImageContextMenu();
    this.disableFolderContextMenu();
    this.imageSelected = JSON.parse(JSON.stringify(item));

    let allImages = document.querySelectorAll(".fb-image-item");
    allImages.forEach(item => item.classList.remove('active'));

    selectedItem.classList.add('active');


  }

  clearSelectImage() {
    this.imageSelected = {
      path: '',
      name: '',
      data: '',
      date: '',
      size: 0
    };

    let allImages = document.querySelectorAll(".fb-image-item");
    allImages.forEach(item => item.classList.remove('active'));
  }

  doClickPreviewImageBtn(e) {
    e.stopPropagation();
    if (this.imageSelected
      && (
        (this.imageSelected.data && this.imageSelected.data.trim() !== '')
        || (this.imageSelected.path && this.imageSelected.path.trim() !== '')
      )) {
      this.previewImage({ data: this.imageSelected.data, path: this.imageSelected.path });
    }
    return;
  }

  doClickChooseImageBtn(e) {
    if (e != null)
      e.stopPropagation();
    if (this.imageSelected
      && (
        (this.imageSelected.data && this.imageSelected.data.trim() !== '')
        || (this.imageSelected.path && this.imageSelected.path.trim() !== '')
      )) {
      this.dialogRef.close(this.imageSelected.path)
    }
    return;
  }

  // CONTEXTMENU ================================================================================================================================

  // config ==========
  handlerFolderContextMenuConfig(event) {
    this.folderContextmenuX = event.clientX
    this.folderContextmenuY = event.clientY
    this.folderContextmenu = true;
    this.disableImageContextMenu();
  }

  handlerImageContextMenuConfig(event, item: ResFileBrowserGetFileDto, selectedItem: HTMLElement) {
    this.imageContextmenuX = event.clientX
    this.imageContextmenuY = event.clientY
    this.imageContextmenu = true;
    this.imageSelected = JSON.parse(JSON.stringify(item));
    let allImages = document.querySelectorAll(".fb-image-item");
    allImages.forEach(item => item.classList.remove('active'));
    selectedItem.classList.add('active');
    this.disableFolderContextMenu();
  }

  // disable ==========
  disableFolderContextMenu() {
    this.folderContextmenu = false;
  }

  disableImageContextMenu() {
    this.imageContextmenu = false;
  }

  // handler action ==========
  handlerFolderContextMenuAction(e: ContextMenuAction) {
    if (this.folderSelected.path === this.simpleMenuData[0].path) {
      this.commonService.openErrorDialog("Folder was Protected", "You not enough permission to change this folder!");
      return;
    }
    this.disableFolderContextMenu();

    switch (e) {

      case ContextMenuAction.RENAME: {
        let data: FileBrowserFolderEditData = {
          name: this.folderSelected.name,
          nameOrigin: this.folderSelected.name
        }
        this.dialogService.open(FileBrowserFolderEditDialogComponent, {
          autoFocus: false,
          data: data
        }).subscribe(
          (data) => {
            if (data) {
            }
          }
        )
        break;
      }

      case ContextMenuAction.MOVE: {
        break;
      }

      case ContextMenuAction.DELETE: {
        break;
      }

      default: break;
    }
    return;

  }

  handlerImageContextMenuAction(e: ContextMenuAction) {
    this.disableImageContextMenu();
    switch (e) {

      case ContextMenuAction.CHOOSE: {
        this.doClickChooseImageBtn(null);
        break;
      }

      case ContextMenuAction.PREVIEW: {
        this.previewImage({ path: this.imageSelected.path })
        break;
      }

      case ContextMenuAction.DELETE: {
        this.doDeleteImage(null);
        break;
      }

      default: break;
    }
    return;

  }

  // IMAGES UPLOAD ------------------------------------------------------------------------------------------------------------------------------------

  doAddImage(event: any) {
    let files = event.target.files;

    for (let file of files) {
      if (this.commonService.isImageSizeUploadError(file)) {
        this.imageErrorList.push(file.name);
        continue;
      }
      let fileReader = new FileReader();
      fileReader.readAsDataURL(file);
      fileReader.onload = () => {
        let image: Image = {
          name: file.name,
          data: fileReader.result.toString().split(',')[1],
          size: Math.round(file.size / 1024)
        }
        this.addImageToList(image);
      };
    }

    event.target.value = "";
    this.imageErrorList = this.snackBarService.openImageSizeErrorSnackBar(this.imageErrorList);
  }

  addImageToList(data: Image) {
    if (data == undefined || data == null || data.name == undefined || data.name == null || data.name.trim() == '')
      return;
    for (let item of this.listImageUpload) {
      if (data.name == item.name && (item.path == undefined || item.path == null || item.path.trim() == '')) {
        item = data;
        return;
      }
    }
    this.listImageUpload.push(data);
    return;
  }

  removeImageFormList(data: Image) {
    this.listImageUpload.forEach((value, index) => {
      if (
        (data.path != undefined && data.path != null && data.path != "")
        && ((value.path != undefined && value.path != null && value.path != ""))
        && data.path == value.path
      ) {
        this.listImageUpload.splice(index, 1);
        return;
      } else if (
        (data.data != undefined && data.data != null && data.data != "")
        && data.name == value.name
      ) {
        this.listImageUpload.splice(index, 1);
        return;
      }
    })
  }

  previewImage(data: Image) {
    this.dialogService.open(ImagePreviewDialogComponent, {
      data: {
        path: this.apiUrlReadImage + "/" + data.path,
        base64: data.data,
        // enableDeleteButton: true
      },
      autoFocus: false,
      disableClose: true
    });
  }

  doClickCancelUploadImage() {
    this.listImageUpload = [];
  }

  doUploadImages() {
    if (!this.folderSelected.name && this.folderSelected.name.trim() === '')
      return;
    let req: ReqFileBrowserAddImage = {
      fileBrowserAddImageDto: {
        currentFolder: this.folderSelected.path,
        imageSaveDtos: this.listImageUpload.map(image => new ReqImageSaveDto(image))
      },
      imageConfigDto: this.initImageConfigDto(),
      folderConfigRequest: this.initFolderConfigDto()
    }
    this.subscribeSearch = this.fileBrowserService.addImages(req)
      .subscribe(
        data => {
          this.listImageUpload = [];
          this.fileList = data.files;
          if (!this.commonService.hasError(data)) {
            this.commonService.openNotificationDialog("Upload Images", "Upload image success");
          }
        }
      )
  }

  doDeleteImage(e) {
    if (e != null)
      e.stopPropagation();
    this.disableFolderContextMenu();
    this.disableImageContextMenu();
    if (!this.imageSelected.path || this.imageSelected.path.trim() === '')
      return


    this.subscriptionConfirm = this.commonService.openYesNoDialog("Delete this image", "Delete this image?")
      .subscribe(
        data => {
          this.subscriptionConfirm.unsubscribe;
          this.subscriptionConfirm = undefined;

          if (data != true) {
            return;
          }
          else {
            let req: ReqFileBrowserDeleteImage = {
              fileBrowserDeleteImageDto: {
                currentFolder: this.folderSelected.path,
                imageDeleteDtos: [
                  {
                    path: this.imageSelected.path
                  }
                ]
              },
              folderConfigRequest: this.initFolderConfigDto(),
              imageConfigDto: this.initImageConfigDto()
            }

            this.subscribeSearch = this.fileBrowserService.deleteImages(req)
              .subscribe(data => {
                this.clearProgressState();
                this.fileList = data.files;
                if (!this.commonService.hasError(data)) {
                  this.commonService.openNotificationDialog("Delete Image", "Delete image success");
                }
              });
          }
        }
      )


  }

  // COMMON FUNCTION =============================================================================================================

  initFolderConfigDto(): ReqFolderConfig {
    let req: ReqFolderConfig = {
      userRoleFv: this.commonService.getAccessUser().roleName,
      storeCdFv: this.commonService.getAccessUser().storeCd
    }

    if (this.data && this.data.specTarget) {
      req.specTarget = this.data.specTarget
    }
    return req;
  }

  initImageConfigDto(): ImageConfigDto {
    let req: ImageConfigDto = {}
    return req;
  }

  clearProgressState() {
    if (this.subscribeSearch) {
      this.subscribeSearch.unsubscribe();
      this.subscribeSearch = undefined;
    }
  }

  reloadClick() {
    this.dialogRef.close(true);
  }

  onClose() {
    this.dialogRef.close();
  }
}