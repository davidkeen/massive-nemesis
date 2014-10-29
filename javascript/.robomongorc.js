// https://github.com/paralect/robomongo/issues/348#issuecomment-38069899
var toCSV = function(data, options){
  var headers = [];
  var records = [];
  var line;
  var response = '';
  var i, l = data.length, rl=0, j, k;
  var reReplaceTextDelim;
  var processRecord = function(src, rec, prefix){
    var key, val, idx;
    prefix = prefix || '';
    rec = rec || new Array(headers.length);
    for(key in src){
      val = src[key];
      key = prefix+key;
      switch(typeof(val)){
        case('object'):
          if(val instanceof Array){
            processRecord(val, rec, key+options.headerDelim);
            break;
          }else if(val instanceof ObjectId){
            if(!options.includeObjectIdWrapper){
              val = ''+val;
            }
          }else if(!(val instanceof Date)){
            processRecord(val, rec, key+options.headerDelim);
            break;
          }
        default:
          if((idx = headers.indexOf(key))===-1){
            idx = headers.length;
            headers.push(key);
          }
          rec[idx] = val.toString();
      }
    }

    return rec;
  };

  options = options || {};
  options.delim = options.delim || ',';
  options.textDelim = options.textDelim || '"';
  options.eoln = options.eoln || '\r\n';
  options.headerDelim = options.headerDelim || '_';
  options.escapeTextDelim = options.escapeTextDelim || ('\\'+options.textDelim);
  reReplaceTextDelim = new RegExp(options.textDelim, 'gi');

  if(options.includeHeaders === void(0)){
    options.includeHeaders = true;
  }

  for(i=0; i<l; i++){
    line = processRecord(data[i]);
    if(rl < line.length){
      rl = line.length;
    }
    records.push(line);
  }
  if(options.includeHeaders){
    response = options.textDelim + headers.join(options.textDelim + options.delim + options.textDelim) + options.textDelim + options.eoln;
  }
  for(i=0; i<l; i++){
    line = records[i];
    if(line.length<rl){
      line = line.concat(new Array(rl-line.length));
    }
    k = line.length;
    for(j=0; j<k; j++){
      if(line[j] !== void(0)){
        response += options.textDelim + line[j].replace(reReplaceTextDelim, options.escapeTextDelim) + options.textDelim;
      }
      if(j<k-1){
        response += options.delim;
      }
    }
    response += options.eoln;
  }

  return response;
};

DBQuery.prototype.toCSV = function(options){
  var cursor = this;
  var data = [];
  while(cursor.hasNext()){
    data.push(cursor.next());
  }
  print(toCSV(data, options));
};
