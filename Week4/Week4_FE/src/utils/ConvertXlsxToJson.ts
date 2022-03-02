import * as XLSX from 'xlsx';

function ConvertXlsxToJson(file: File) {
	console.log('reachedHere');
	return new Promise<object>((resolve, reject) => {
		const fileReader = new FileReader();
		fileReader.readAsBinaryString(file);
		fileReader.onload = (evt) => {
			const readData = XLSX.read(evt.target?.result, { type: 'binary' });
			const workSheetName = readData.SheetNames[0];
			const workSheet = readData.Sheets[workSheetName];
			const data = XLSX.utils.sheet_to_csv(workSheet);
			resolve(parseCsv(data));
		};
		fileReader.onerror = reject;
	});
}

function parseCsv(str: string): [] {
	const row = str.split('\n');
	let resultString = '[';
	let init = false;
	row.shift();
	row.forEach((r) => {
		const c = r.split(',');
		if (c.length > 1) {
			/* menu and description */
			if (c[0] !== '') {
				if (!init) {
					init = true;
				} else {
					resultString = resultString.slice(0, -1);
					resultString += ']},';
				}
				resultString += `{"name": "${c[0]}", "description": "${c[3]}", "sizes": [`;
			}
			/* size */
			if (c[1] !== '') {
				resultString += `{"size": "${c[1]}", "price":${c[2]}},`;
			} else {
				resultString += `{"size": "", "price":${c[2]}}]},`;
			}
		} else {
			resultString = resultString.slice(0, -1);
			resultString += ']';
		}
	});
	console.log(resultString);
	const ret = JSON.parse(resultString);
	console.log(ret);
	return ret;
}

export default ConvertXlsxToJson;
