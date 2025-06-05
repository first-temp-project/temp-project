window.boardNum = $("#board-container").data("boardNum");
const $thumbnailUl = $("ul.thumbnailUl");
const $fileInput = $("input[name=multipartFiles]");
const fileArray = new Array();
const isUpdate = $fileInput.data("update");
const removeClassName = "remove";
const updateClassName = "new";
const maxSize = 1024 * 1024 * 100;
const maxLength = 3;
const fileService = (function() {
	function upload(formData, callback) {
		$.ajax({
			url: `${contextPath}/files/upload`,
			method: 'post',
			contentType: false,
			processData: false,
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

	function getLength(boardNum, callback) {
		$.ajax({
			url: `${contextPath}/files/length/${boardNum}`,
			method: 'get',
			dataType: 'json',
			async: false,
			success: callback
		});
	}
	return { upload, getFiles, getLength };
})();
let totalFileSize = 0;
$fileInput.on("change", function() {
	const files = Array.from($fileInput[0].files);
	let dbfilesLength = 0;
	if (boardNum) { fileService.getLength(boardNum, length => dbfilesLength += length); }
	console.log(boardNum);
	console.log("db==="+dbfilesLength);
	console.log("files.len=="+files.length);
	console.log("fileArray=="+fileArray.length);
	let currentLength = dbfilesLength + files.length + fileArray.length;
	if (currentLength > maxLength) { alert("파일은 최대 3개까지만 업로드할 수 있습니다."); return; }
	const formData = new FormData();
	const isUpdate = $fileInput.data("update");
	files.forEach(file => {
		if (!validateFile(file.name, file.size)) { return false; }
		fileArray.push(file);
		formData.append("multipartFiles", file);
	});
	isUpdate ? fileService.upload(formData, files => appendThumbnails({ $ul: $thumbnailUl, files, isUpdate })) : fileService.upload(formData, files => appendThumbnails({ $ul: $thumbnailUl, files }));
	resetFileInput($fileInput, fileArray);
});


$thumbnailUl.on("click", "img.file-cancel-btn", function() {
	const $li = $(this).closest("li");
	let index;
	if (!isUpdate) {
		index = $("img.file-cancel-btn").index($(this));
		$li.eq(index).remove();
		removeArrayByIndex($fileInput, fileArray, index);
	}
	index = $(`li.${updateClassName}`).index($li);
	if (index != -1) {
		$li.remove();
		removeArrayByIndex($fileInput, fileArray, index);

	} else {
		$li.attr("class", removeClassName).hide();
	}

});

function getIsUpdate() {
	return isUpdate;
}

function getThumbnailUl() {
	return $thumbnailUl;
}

function getBoardNum() {
	return boardNum;
}

function getRemoveClassName() {
	return removeClassName;
}

function getUpdateClassName() {
	return updateClassName;
}

function removeArrayByIndex($fileInput, fileArray, index) {
	fileArray.splice(index, 1);
	resetFileInput($fileInput, fileArray);
}

function resetFileInput($fileInput, fileArray) {
	const dataTransfer = new DataTransfer();
	fileArray.forEach(file => dataTransfer.items.add(file));
	$fileInput[0].files = dataTransfer.files;
}

function showThumbnails($ul, boardNum, isDownload) {
	fileService.getFiles(boardNum, files => appendThumbnails({ $ul, files, isDownload, isUpdate: false }));
}

function appendThumbnails({ $ul, files, isDownload, isUpdate }) {
	let html = createThumbnails(files, isDownload, isUpdate);
	$ul.append(html);
}

function createFileInfoInputs($ul, className = "original") {
	let name = className === removeClassName ? "deleteFiles" : "files";
	let html = "";
	$ul.find(`li.${className}`).each((i, li) => {
		html += `<input type="hidden" name="${name}[${i}].fileNum" value="${li.dataset.fileNum}" /> `;
		html += `<input type="hidden" name="${name}[${i}].fileUuid" value="${li.dataset.fileUuid}" /> `;
		html += `<input type="hidden" name="${name}[${i}].fileUploadPath" value="${li.dataset.fileUploadPath}" /> `;
		html += `<input type="hidden" name="${name}[${i}].fileName" value="${li.dataset.fileName}" /> `;
		html += `<input type="hidden" name="${name}[${i}].fileType" value="${li.dataset.fileType}" /> `;
		html += `<input type="hidden" name="${name}[${i}].fileSize" value="${li.dataset.fileSize}" /> `;
	});
	return html;
}

function createThumbnails(files, isDownload = false, isUpdate = false) {
	let className = isUpdate ? updateClassName : 'original';
	let html = "";
	files.forEach(file => {
		let displayFileName = encodeURIComponent(`${file.fileUploadPath}/t_${file.fileUuid}_${file.fileName}`);
		let downloadFileName = displayFileName.replace("t_", "");
		let fileNum = file.fileNum ? file.fileNum : "";
		html += `<li class="${className}" data-file-num="${fileNum}" data-file-uuid="${file.fileUuid}" data-file-upload-path="${file.fileUploadPath}" data-file-name="${file.fileName}" data-file-type="${file.fileType}" data-file-size="${file.fileSize}"/> `;
		html += isDownload ? `<a href="${contextPath}/files/download?fileName=${downloadFileName}">` : ``;
		html += file.fileType ? `<img src="${contextPath}/files/display?fileName=${displayFileName}" width="100" />` : `<img src="${contextPath}/static/images/attach.png" width="25" />`;
		html += isDownload ? `</a>` : ``;
		html += !isDownload ? `<img class="file-cancel-btn" src="${contextPath}/static/images/cancel.png" width="25" />` : ``;
		html += `</li>`;
	});
	return html;
}


function validateFileSize(fileSize) {
	totalFileSize += fileSize;
	if (totalFileSize > maxSize) {
		alert("업로드 가능한 용량을 초과하였습니다.");
		totalFileSize -= fileSize;
		return false;
	}
	return true;
}

function validateFile(fileName, fileSize) {
	if (!validateFileSize(fileSize)) {
		return false;
	}
	let regExp = new RegExp("(.*/)\.(exe|sh|alz)$", "i");
	if (regExp.test(fileName)) {
		alert("업로드 가능한 파일 형식이 아닙니다.");
		return false;
	}
	return true;
}
