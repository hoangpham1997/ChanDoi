export interface IContextMenu {
    data?: any;
    selectedData?: any;
    x?: any;
    y?: any;
    isShow?: any;
    isNew?: boolean;
    isDelete?: boolean;
    isCopy?: boolean;
    event?: any;
    checkTag?: number;
}

export class ContextMenu implements IContextMenu {
    constructor(
        public data?: any,
        public selectedData?: any,
        public x?: any,
        public y?: any,
        public isShow?: any,
        public isNew?: boolean,
        public isDelete?: boolean,
        public isCopy?: boolean,
        public event?: any,
        public customActionAddNewRow?: any,
        public customActionDeleteRow?: any,
        public checkTag?: number
    ) {
        this.x = 0;
        this.y = 0;
        this.isShow = false;
        this.isNew = true;
        this.isDelete = true;
    }
}
