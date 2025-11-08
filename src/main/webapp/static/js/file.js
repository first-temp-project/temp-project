const fileService = (function() {
	function upload(formData, category, callback) {
		const $dimmed = $(".dimmed-container");

		$.ajax({
			url: `${contextPath}/files/upload?category=${category}`,
			method: 'post',
			contentType: false,
			processData: false,
			beforeSend() { $dimmed.show() },
			complete() { $dimmed.hide() },
			data: formData,
			success: callback
		});
	}

	function getFiles(boardNum, callback) {
		$.ajax({
			url: `${contextPath}/files/${boardNum}`,
			method: 'get',
			success: callback
		});
	}

	function count(boardNum, callback) {
		$.ajax({
			url: `${contextPath}/files/count/${boardNum}`,
			method: 'get',
			async: false,
			success: callback
		});
	}

	return { upload, getFiles, count };

})();

export const classNames = { ORIGINAL: "original", NEW: "new", REMOVE: "remove" };
const $fileInput = $("input[name=multipartFiles]");
const $thumbnailUl = $("ul.thumbnail-ul");
const fileSizeArray = [];
const fileArray = [];
let fileTotalCount = 0;
let fileTotalSize = 0;
let fileRemoveCount = 0;

$fileInput.on("change", function() {
	const boardNum = $(".container").data("boardNum");
	const formData = new FormData();
	const category = getCategory();
	const files = Array.from($fileInput[0].files);
	const fileMaxCount = 2;
	fileTotalCount = files.length + fileArray.length + fileRemoveCount;
	if (boardNum) { fileService.count(boardNum, count => fileTotalCount += count); }
	if (fileMaxCount < fileTotalCount) {
		alert("파일은 2개까지만 업로드 가능합니다.");
		refreshFileInput(fileArray, fileSizeArray);
		return;
	}

	for (const file of files) {
		if ("download" != category && !isImage(file.type)) { rejectUpload("이미지 형식만 업로드 가능합니다", fileArray, fileSizeArray); return; }
		if (!validateFileSize(file.size)) { rejectUpload("업르드가능한 사이즈를 초과하엿습니다.", fileArray, fileSizeArray); return; }
		if (!validateFileName(file.name)) { rejectUpload("업로드 불가능한 파일 형식 입니다.", fileArray, fileSizeArray); return; }
		fileArray.push(file);
		fileSizeArray.push(file.size);
		formData.append("multipartFiles", file);
	}

	fileService.upload(formData, category, files => isUpdate() ? appendThumbnails(files, true) : appendThumbnails(files, false));
	refreshFileInput(fileArray, fileSizeArray);
});

$thumbnailUl.on("click", ".file-cancel-btn", function(e) {
	e.preventDefault();
	const $cancelBtn = $(this);
	const $li = $cancelBtn.closest("li");
	let index;

	if (!isUpdate()) {
		index = $(".file-cancel-btn").index($cancelBtn);
		$li.remove();
		refreshFileInput(fileArray, fileSizeArray, index);
		return;
	}

	index = $(`li.${classNames.NEW}`).index($li);
	if (index != -1) {
		$li.remove();
		refreshFileInput(fileArray, fileSizeArray, index);
		return;
	}

	$li.attr("class", classNames.REMOVE).hide();
	--fileRemoveCount;
});


export function showThumbnails(boardNum, isDownload) {
	fileService.getFiles(boardNum, files => appendThumbnails(files, false, isDownload));
}

function appendThumbnails(files, isUpdate, isDownload) {
	const html = createThumbnails(files, isUpdate, isDownload);
	$thumbnailUl.append(html);
}

function createThumbnails(files, isUpdate = false, isDownload = false) {
	if (!files) { return ""; }
	const category = getCategory();
	let html = "";

	files.forEach(file => {
		const displayFilePath = `${file.fileUploadPath}/t_${file.fileUuid}_${file.fileName}`;
		const downloadFilePath = displayFilePath.replace("t_", "");
		html += `<li class="${isUpdate ? classNames.NEW : classNames.ORIGINAL}" data-file-num="${file.fileNum ? file.fileNum : ''}" data-file-uuid="${file.fileUuid}" data-file-upload-path="${file.fileUploadPath}" data-file-name="${file.fileName}" data-file-size="${file.fileSize}" data-file-type="${file.fileType}" />`;
		html += isDownload ? `<a href="${contextPath}/files/download?filePath=${downloadFilePath}&category=${category}">` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?filePath=${displayFilePath}&category=${category}" width="100" />` : `<img src="${contextPath}/static/images/attach.png" width="100" />`;
		html += isDownload ? `</a>` : ``;
		html += !isDownload ? `<img class="file-cancel-btn" src="${contextPath}/static/images/cancel.png" width="25" />` : ``;
		html += `</li>`;
	});

	return html;
}

export function createHiddenInputs(className = classNames.ORIGINAL) {
	const name = className == classNames.REMOVE ? 'deleteFiles' : 'insertFiles';
	let html = "";
	$(`li.${className}`).each((i, li) => {
		html += `<input type="hidden" name="${name}[${i}].fileNum" value="${li.dataset.fileNum}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUuid" value="${li.dataset.fileUuid}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileUploadPath" value="${li.dataset.fileUploadPath}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileName" value="${li.dataset.fileName}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileSize" value="${li.dataset.fileSize}" />`;
		html += `<input type="hidden" name="${name}[${i}].fileType" value="${li.dataset.fileType}" />`;
	});
	return html;
}

function refreshFileInput(fileArray, fileSizeArray, index = -1) {
	const dataTransfer = new DataTransfer();
	if (index >= 0) {
		removeFile(fileArray, index);
		removeFileSize(fileSizeArray, index);
	}
	fileArray.forEach(file => dataTransfer.items.add(file));
	$("input[name=multipartFiles]")[0].files = dataTransfer.files;
}

function removeFile(fileArray, index) {
	fileArray.splice(index, 1);
}

function removeFileSize(fileSizeArray, index) {
	console.log("삭제전",fileTotalSize);
	fileTotalSize -= fileSizeArray[index];
	fileSizeArray.splice(index, 1);
	console.log("삭제후",fileTotalSize);
}

export function isUpdate() {
	return $(".container").data("boardUpdate");
}

export function getCategory() {
	return $(".container").data("boardCategory");
}

function isImage(fileType) {
	return fileType && fileType.startsWith("image/");
}

function validateFileSize(fileSize) {
	const maxFileSize = 1024 * 1024 * 50;
	if (maxFileSize < fileTotalSize + fileSize) {
		return false;
	}
	fileTotalSize += fileSize;
	return true;
}

function validateFileName(fileName) {
	let regExp = new RegExp("(.*/)\.(exe|sh|alz)$", "i");
	if (regExp.test(fileName)) {
		return false;
	}
	return true;
}

function rejectUpload(msg, fileArray, fileSizeArray) {
	alert(msg);
	refreshFileInput(fileArray, fileSizeArray);
}



