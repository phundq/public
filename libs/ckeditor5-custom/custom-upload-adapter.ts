import { DialogService } from './../dialog/dialog-service';
export class CustomUploadAdapter {
  /**
   * Creates a new adapter instance.
   *
   * @param {module:upload/filerepository~FileLoader} loader
   */

  loader: any;
  reader: any;
  fileSize: number;
  dialogService: DialogService;
  constructor(loader, fileSize: number, dialogServiceI: DialogService) {
    /**
     * `FileLoader` instance to use during the upload.
     *
     * @member {module:upload/filerepository~FileLoader} #loader
     */
    this.loader = loader;
    this.fileSize = fileSize;
    this.dialogService = dialogServiceI
  }

  /**
   * Starts the upload process.
   *
   * @see module:upload/filerepository~UploadAdapter#upload
   * @returns {Promise}
   */
  upload() {
    return new Promise((resolve, reject) => {
      // reject(null)
      const reader = this.reader = new FileReader();

      reader.addEventListener('load', () => {
        resolve({ default: reader.result });
      });

      reader.addEventListener('error', err => {
        reject(err);
      });

      reader.addEventListener('abort', () => {
        reject();
      });

      this.loader.file.then(file => {
        if (file.size <= 1024 * 1024 * this.fileSize) {
          reader.readAsDataURL(file);
        }
        else {
          reject("file's size too big to upload");
        }
      });
    });
  }

  /**
   * Aborts the upload process.
   *
   * @see module:upload/filerepository~UploadAdapter#abort
   * @returns {Promise}
   */
  abort() {
    this.reader.abort();
  }
}
