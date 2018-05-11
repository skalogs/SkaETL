const faviconsContext = require.context(
  '!!file-loader?name=assets/favicons/[name].[ext]!.',
  true,
  /\.(svg|png|ico|xml|json)$/
);
faviconsContext.keys().forEach(faviconsContext);
